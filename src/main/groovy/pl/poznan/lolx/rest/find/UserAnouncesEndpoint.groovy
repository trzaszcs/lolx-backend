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
class UserAnouncesEndpoint {

    AnounceSearchService anounceSearchService
    SearchResultMapper searchResultMapper

    @Autowired
    UserAnouncesEndpoint(
            AnounceSearchService anounceSearchService,
            SearchResultMapper searchResultMapper) {
        this.anounceSearchService = anounceSearchService
        this.searchResultMapper = searchResultMapper
    }

    @RequestMapping(value = "/anounces/user", method = RequestMethod.GET)
    SearchResultDto find(@RequestParam("userId") String userId,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         @RequestParam(name = "itemsPerPage", defaultValue = "20") int itemsPerPage) {
        log.info("find anounces by query: {}", query)
        searchResultMapper.map(anounceSearchService.forUser(userId, page, itemsPerPage))
    }

}
