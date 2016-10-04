package pl.poznan.lolx.infrastructure

import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.*

@Component
class SearchEngineInMemory implements SearchEngine {

    final static int MAX_ITEMS_PER_PAGE = 50

    def indexedAnounces = [generateSingle(0, "Wyprowadzę psa", 666), generateSingle(1, "Skoszę trawę", 666), generateSingle(2, "Udziele korepetycji", 666)]

    @Override
    SearchResult find(String phrase, int page, int itemsPerPage, Optional<Coordinate> coordinateOpt) {
        itemsPerPage = itemsPerPage < MAX_ITEMS_PER_PAGE ? itemsPerPage : MAX_ITEMS_PER_PAGE
        def anounces = []
        anounces.addAll(indexedAnounces)
//        anounces.addAll(generateAnounces(itemsPerPage, (page * itemsPerPage)))
        new SearchResult(
                totalCount: anounces.size(),
                anounces: anounces
        )
    }

    @Override
    SearchResult forUser(String userId, int page, int itemsPerPage) {
        def anounces = []
        anounces.addAll(indexedAnounces)
//        anounces.addAll(generateAnounces(2, 0, userId))
        new SearchResult(
                totalCount: anounces.size(),
                anounces: anounces
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

    private def generateAnounces(count, offset, ownerId = "someOwner") {
        def anounces = []
        (0..count).each {
            anounces.add(generateSingle("${offset + it}", generateTitle(it), ownerId))
        }
        return anounces
    }

    def genPrice() {
        Math.round(Math.random() * 100) / 100;
    }

    def generateSingle(id, title, ownerId) {
        new Anounce(id: id, title: title, description: "Lorem Ipsum ...", location: new Location("Poznan, wielkopolskie", 52.406374, 16.9251681), ownerId: ownerId, ownerName: "someName", price: genPrice())
    }

    def generateTitle(id) {
        def dictionary = ["super", "dokładnie", "każdą", "najtaniej", "expresowo", "każdego dnia", "zawodowo", "najlepiej"]
        def verb = ["sprzątam", "myję", "przepycham", "odkażam", "udrażniam", "woskuję", "maluję", "poleruję", "zamiatam wokół"]
        def subject = ["toaletę", "kabinę", "zlew", "rurę", "łazienkę", "twojego zwierzaka", "garaż", "werandę", "piwnicę i strych", "chodnik przed twoim domem", "twojego pupila"]
        Random random = new Random();
        def title = "${dictionary[random.nextInt(dictionary.size())]} ${dictionary[random.nextInt(dictionary.size())]} " +
                "${verb[random.nextInt(verb.size())]} ${subject[random.nextInt(subject.size())]} #${id}"
        title
    }
}
