package pl.poznan.lolx.infrastructure.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import pl.poznan.lolx.domain.ClientException
import pl.poznan.lolx.domain.Location
import pl.poznan.lolx.domain.add.User
import pl.poznan.lolx.domain.add.UserClient
import pl.poznan.lolx.domain.add.UserDetails
import pl.poznan.lolx.infrastructure.shared.AuthorizationHeaderBuilder

@Component
class UserClientRestService implements UserClient {

    @Value('${user-service.addr}')
    String serviceAddress

    @Autowired
    RestTemplate restTemplate

    @Autowired
    AuthorizationHeaderBuilder authorizationHeaderBuilder

    @Override
    Optional<User> find(String id, boolean detailed) {
        HttpHeaders headers = new HttpHeaders();
        if (detailed) {
            headers.set("Authorization", authorizationHeaderBuilder.build(id))
        }
        HttpEntity entity = new HttpEntity(headers);
        try {
            def response = restTemplate.exchange(
                    "${serviceAddress}/users/${id}",
                    HttpMethod.GET,
                    entity,
                    UserDto)
            return Optional.of(map(response.body, detailed))
        } catch (HttpServerErrorException ex) {
            throw new ClientException("cound not get user $id details", ex)
        } catch (HttpClientErrorException ex) {
            if (ex.statusCode == 404)
                return Optional.empty()
            throw ex
        }

    }

    @Override
    Map<String, User> find(List<String> ids) {
        try {
            def uri = UriComponentsBuilder
                    .fromHttpUrl("${serviceAddress}/users/bulk")
                    .queryParam("userId", ids.toArray())
                    .build().encode().toString()
            def response = restTemplate
                    .exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, UserDto>>() {})
            def usersMap = response.body
            return usersMap.keySet().collectEntries {
                [(it): map(
                        Optional.ofNullable(usersMap[it]).orElseThrow({new RuntimeException("User with id ${it} not found") }),
                        false
                )]
            }
        } catch (HttpClientErrorException ex) {
            if (ex.statusCode == 404)
                return Optional.empty()
            throw ex
        }
    }

    private User map(UserDto dto, boolean detailed) {
        UserDetails details
        if (detailed) {
            def locationDto = dto.location
            details = new UserDetails(
                    email: dto.email,
                    photoUrl: dto.photoUrl,
                    location: new Location(locationDto.title, locationDto.latitude, locationDto.longitude))
        }
        new User(id: dto.id, created: dto.created, firstName: dto.firstName, nick: dto.nick, userDetails: details)
    }
}
