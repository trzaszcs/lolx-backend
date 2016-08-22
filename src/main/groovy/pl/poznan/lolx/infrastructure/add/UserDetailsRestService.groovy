package pl.poznan.lolx.infrastructure.add

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import pl.poznan.lolx.domain.add.User
import pl.poznan.lolx.domain.add.UserDetails

@Component
class UserDetailsRestService implements UserDetails {

    @Value('${user-service.addr}')
    String userServiceAddress

    @Autowired
    RestTemplate restTemplate

    @Override
    Optional<User> find(String id) {
        try {
            def response = restTemplate.getForEntity("${userServiceAddress}/users/${id}", UserDto)
            return Optional.of(new User(id: id, email: response.body.email, created: response.body.created))
        } catch (HttpClientErrorException ex) {
            if (ex.statusCode == 404)
                return Optional.empty()
            throw ex
        }
    }
}
