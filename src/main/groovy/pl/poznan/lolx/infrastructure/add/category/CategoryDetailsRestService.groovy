package pl.poznan.lolx.infrastructure.add.category

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import pl.poznan.lolx.domain.add.Category
import pl.poznan.lolx.domain.add.CategoryDetails
import pl.poznan.lolx.domain.add.User
import pl.poznan.lolx.domain.add.UserDetails
import pl.poznan.lolx.infrastructure.add.user.UserDto

@Component
class CategoryDetailsRestService implements CategoryDetails {

    @Value('${user-service.addr}')
    String categoryAddress

    @Autowired
    RestTemplate restTemplate

    @Override
    Optional<Category> find(String id) {
        try {
            def response = restTemplate.getForEntity("${categoryAddress}/categories/${id}", CategoryDto)
            return Optional.of(new Category(id: id, name: response.body.name))
        } catch (HttpClientErrorException ex) {
            if (ex.statusCode == 404)
                return Optional.empty()
            throw ex
        }
    }
}
