package pl.poznan.lolx.rest.delete

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.poznan.lolx.domain.AnounceSearchService
import pl.poznan.lolx.domain.jwt.JwtChecker

@RestController
@Slf4j
class DeleteAnounceEndpoint {

    AnounceSearchService anounceSearchService
    JwtChecker jwtChecker

    @Autowired
    DeleteAnounceEndpoint(
            AnounceSearchService anounceSearchService,
            JwtChecker jwtChecker) {
        this.anounceSearchService = anounceSearchService
        this.jwtChecker = jwtChecker
    }

    @RequestMapping(value = "/anounces/{anounceId}", method = RequestMethod.DELETE)
    ResponseEntity add(
            @RequestHeader(value = "Authorization") authorizationHeader,
            @PathVariable String anounceId) {
        log.info("got delete anounce {}", anounceId)

        def anounce = anounceSearchService.getById(anounceId);
        def ownerId = anounce.get().ownerId

        if (!jwtChecker.verify(authorizationHeader, ownerId)) {
            log.warn("rejecting delete anounce {} due to authorization error", anounce)
            return new ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        new ResponseEntity(HttpStatus.NO_CONTENT)
    }

}
