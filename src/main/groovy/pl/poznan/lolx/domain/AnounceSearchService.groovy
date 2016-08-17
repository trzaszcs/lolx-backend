package pl.poznan.lolx.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AnounceSearchService {

    @Autowired
    SearchEngine searchEngine

    SearchResult find(String phrase, int page, int itemsPerPage) {
        searchEngine.find(phrase, page, itemsPerPage)
    }

    SearchResult forUser(String userId, int page, int itemsPerPage) {
        searchEngine.forUser(userId, page, itemsPerPage)
    }

    SearchResult getById(String anounceId) {
        searchEngine.getById(anounceId)
    }

}
