package pl.poznan.lolx.infrastructure.add.category

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import pl.poznan.lolx.domain.add.Category
import pl.poznan.lolx.domain.add.CategoryDetails

@Component
class CategoryDetailsRestService implements CategoryDetails {

    @Value('${category-service.addr}')
    String categoryAddress

    @Autowired
    RestTemplate restTemplate

    @Override
    Optional<Category> find(String id) {
        try {
            def response = restTemplate.getForEntity("${categoryAddress}/categories/${id}", CategoryDto)
            return Optional.of(new Category(id: id, name: response.body.name))
        } catch (HttpClientErrorException ex) {
            if (ex.rawStatusCode == 404)
                return Optional.empty()
            throw ex
        }
    }
}
