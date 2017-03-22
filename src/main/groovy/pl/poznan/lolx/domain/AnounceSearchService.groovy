package pl.poznan.lolx.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AnounceSearchService {

    @Autowired
    SearchEngine searchEngine

    SearchResult<Anounce> find(
            String phrase,
            AnounceType type,
            int page,
            int itemsPerPage,
            String location,
            Integer latitude,
            Integer longitude,
            String categoryId) {
        searchEngine.find(phrase, type, page, itemsPerPage, getCoordinate(latitude, longitude).orElse(null), categoryId)
    }

    SearchResult<Anounce> forUser(String userId, int page, int itemsPerPage) {
        searchEngine.forUser(userId, page, itemsPerPage)
    }

    Optional<Anounce> getById(String anounceId) {
        searchEngine.getById(anounceId)
    }

    Optional<Coordinate> getCoordinate(Integer latitude,
                                       Integer longitude) {

        if (latitude && longitude) {
            return Optional.of(new Coordinate(latitude, longitude))
        }
        return Optional.empty()
    }

    void delete(String anounceId) {
        searchEngine.delete(anounceId)
    }

}
