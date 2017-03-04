package pl.poznan.lolx.infrastructure

import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.*

@Component
class SearchEngineInMemory implements SearchEngine {

    final static int MAX_ITEMS_PER_PAGE = 50

    def indexedAnounces = []

    @Override
    SearchResult find(String phrase, AnounceType type, int page, int itemsPerPage, Optional<Coordinate> coordinateOpt, Optional<String> categoryId) {
        itemsPerPage = itemsPerPage < MAX_ITEMS_PER_PAGE ? itemsPerPage : MAX_ITEMS_PER_PAGE
        def anounces = []
        anounces.addAll(indexedAnounces)
//        anounces.addAll(generateAnounces(itemsPerPage, (page * itemsPerPage)))
        new SearchResult(
                totalCount: anounces.size(),
                items: anounces
        )
    }

    @Override
    SearchResult forUser(String userId, int page, int itemsPerPage) {
        def userAnounces = indexedAnounces
                .findAll { it.ownerId == userId }
        def endingIndex = (page + 1) * itemsPerPage
        def pageResults = userAnounces.subList(
                page * itemsPerPage,
                userAnounces.size() > endingIndex ? endingIndex : userAnounces.size())
        new SearchResult(
                totalCount: userAnounces.size(),
                items: pageResults
        )
    }

    @Override
    Optional<Anounce> getById(String anounceId) {
        def anounce = Optional.ofNullable(indexedAnounces.find { it.id == anounceId }) as Optional<Anounce>
        if (anounce.isPresent()) {
            return anounce
        }
        return Optional.empty()
    }

    @Override
    void index(Anounce anounce) {
        indexedAnounces.add(anounce)
    }

    @Override
    void delete(String id) {
        def iter = indexedAnounces.iterator()
        while (iter.hasNext()) {
            def anounce = iter.next()
            if (anounce.id == id) {
                iter.remove()
                return
            }
        }
    }
}
