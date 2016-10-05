package pl.poznan.lolx.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AnounceSearchService {

    @Autowired
    SearchEngine searchEngine

    SearchResult find(
            String phrase,
            int page,
            int itemsPerPage,
            Optional<String> location,
            Optional<Integer> latitude,
            Optional<Integer> longitude,
            Optional<String> categoryId) {
        searchEngine.find(phrase, page, itemsPerPage, getCoordinate(latitude, longitude), categoryId)
    }

    SearchResult forUser(String userId, int page, int itemsPerPage) {
        searchEngine.forUser(userId, page, itemsPerPage)
    }

    Optional<Anounce> getById(String anounceId) {
        searchEngine.getById(anounceId)
    }

    Optional<Coordinate> getCoordinate(Optional<Integer> latitude,
                                       Optional<Integer> longitude) {
        if (latitude.isPresent() && longitude.isPresent()) {
            return Optional.of(new Coordinate(latitude.get(), longitude.get()))
        }
        return Optional.empty()
    }

}
