package pl.poznan.lolx.rest.find

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.poznan.lolx.domain.AnounceSearchService

@RestController
@Slf4j
class FindAnouncesEndpoint {

    AnounceSearchService anounceSearchService
    SearchResultMapper searchResultMapper

    @Autowired
    FindAnouncesEndpoint(
            AnounceSearchService anounceSearchService,
            SearchResultMapper searchResultMapper) {
        this.anounceSearchService = anounceSearchService
        this.searchResultMapper = searchResultMapper
    }

    @RequestMapping(value = "/anounces", method = RequestMethod.GET)
    SearchResultDto find(@RequestParam("query") String query,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         @RequestParam(name = "itemsPerPage", defaultValue = "20") int itemsPerPage,
                         @RequestParam(name = "location", required = false) String location,
                         @RequestParam(name = "latitude", required = false) Integer latitude,
                         @RequestParam(name = "longitude", required = false) Integer longitude,
                         @RequestParam(name = "categoryId", required = false) String categoryId) {
        log.info("find anounces by query: {} and location {}", query, location)
        searchResultMapper.map(
                anounceSearchService.find(
                        query,
                        page,
                        itemsPerPage,
                        location,
                        latitude,
                        longitude,
                        categoryId))
    }
}
