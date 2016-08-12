package pl.poznan.lolx.rest.find

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import pl.poznan.lolx.domain.AnounceSearchService

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

    @RequestMapping(value = "/anounces/user/{userId}", method = RequestMethod.GET)
    SearchResultDto find(@PathVariable("userId") String userId,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         @RequestParam(name = "itemsPerPage", defaultValue = "20") int itemsPerPage) {
        searchResultMapper.map(anounceSearchService.forUser(userId, page, itemsPerPage))
    }

}
