package pl.poznan.lolx.domain

import org.springframework.stereotype.Component

@Component
class AnounceSearchService {

    final static int MAX_ITEMS_PER_PAGE = 50

    SearchResult find(String phrase, int page, int itemsPerPage) {
        itemsPerPage = itemsPerPage < MAX_ITEMS_PER_PAGE ? itemsPerPage : MAX_ITEMS_PER_PAGE
        new SearchResult(
                totalCount: 100,
                anounces: generateAnounces(itemsPerPage, (page * itemsPerPage))
        )
    }

    private def generateAnounces(count, offset) {
        def anounces = []
        (0..count).each {
            anounces.add(new Anounce(id: "${offset + it}", title: "title ${offset + it}", state: "wlkp", city: "Poz"))
        }
        return anounces
    }
}
