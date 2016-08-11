package pl.poznan.lolx.rest.find

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.poznan.lolx.domain.AnounceSearchService
import pl.poznan.lolx.domain.SearchResult

@RestController
@Slf4j
class FindAnouncesEndpoint {

    AnounceSearchService anounceSearchService

    @Autowired
    FindAnouncesEndpoint(AnounceSearchService anounceSearchService) {
        this.anounceSearchService = anounceSearchService
    }

    @RequestMapping(value = "/anounces", method = RequestMethod.GET)
    SearchResultDto find(@RequestParam("query") String query,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         @RequestParam(name = "itemsPerPage", defaultValue = "20") int itemsPerPage) {
        log.info("find anounces by query: {}", query)
        map(anounceSearchService.find(query, page, itemsPerPage))
    }

    def map(SearchResult searchResult) {
        new SearchResultDto(
                totalCount: searchResult.totalCount,
                anounces: searchResult.anounces.collect {
                    new SimpleAnounceDto(id: it.id, title: it.title, state: it.state, city: it.city)
                }
        )

    }

}
