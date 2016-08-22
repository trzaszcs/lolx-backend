package pl.poznan.lolx.infrastructure

import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.Anounce
import pl.poznan.lolx.domain.SearchEngine
import pl.poznan.lolx.domain.SearchResult

@Component
class SearchEngineInMemory implements SearchEngine {

    final static int MAX_ITEMS_PER_PAGE = 50

    def indexedAnounces = []

    @Override
    SearchResult find(String phrase, int page, int itemsPerPage) {
        itemsPerPage = itemsPerPage < MAX_ITEMS_PER_PAGE ? itemsPerPage : MAX_ITEMS_PER_PAGE
        def anounces = []
        anounces.addAll(indexedAnounces)
        anounces.addAll(generateAnounces(itemsPerPage, (page * itemsPerPage)))
        new SearchResult(
                totalCount: 100,
                anounces: anounces
        )
    }

    @Override
    SearchResult forUser(String userId, int page, int itemsPerPage) {
        def anounces = []
        anounces.addAll(indexedAnounces)
        anounces.addAll(generateAnounces(2, 0, userId))
        new SearchResult(
                totalCount: 2,
                anounces: anounces.subList(0, 2)
        )
    }

    @Override
    Optional<Anounce> getById(String anounceId) {
        new Anounce(id: anounceId,
                title: "title ${anounceId}",
                state: "wlkp",
                city: "Poz",
                description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin nibh augue, suscipit a, scelerisque sed, lacinia in, mi. Cras vel lorem. Etiam pellentesque aliquet tellus. Phasellus pharetra nulla ac diam. Quisque semper justo at risus. Donec venenatis, turpis vel hendrerit interdum, dui ligula ultricies purus, sed posuere libero dui id orci. Nam congue, pede vitae dapibus aliquet, elit magna vulputate arcu, vel tempus metus leo non est. Etiam sit amet lectus quis est congue mollis."
        )
    }

    @Override
    void index(Anounce anounce) {
        indexedAnounces.add(anounce)
    }

    private def generateAnounces(count, offset, ownerId = "someOwner") {
        def anounces = []
        (0..count).each {
            anounces.add(generateSingle("${offset + it}", "${offset + it}", owner))
        }
        return anounces
    }

    private def generateSingle(id, title, ownerId) {
        new Anounce(id: id, title: title, description: "Lorem Ipsum ...", state: "wlkp", city: "Poz", ownerId: ownerId, ownerName: "someName")
    }
}
