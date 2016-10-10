package pl.poznan.lolx.infrastructure.add.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import pl.poznan.lolx.domain.add.Category
import pl.poznan.lolx.domain.add.User
import pl.poznan.lolx.domain.add.UserDetails
import pl.poznan.lolx.infrastructure.add.category.CategoryDto

@Component
class UserDetailsRestService implements UserDetails {

    @Value('${user-service.addr}')
    String serviceAddress

    @Autowired
    RestTemplate restTemplate

    @Override
    Optional<User> find(String id) {
        try {
            def response = restTemplate.getForEntity("${serviceAddress}/users/${id}", UserDto)
            def dto = response.body
            return Optional.of(new User(id: id, email: dto.email, created: dto.created, firstName: dto.firstName))
        } catch (HttpClientErrorException ex) {
            if (ex.statusCode == 404)
                return Optional.empty()
            throw ex
        }
    }
}
