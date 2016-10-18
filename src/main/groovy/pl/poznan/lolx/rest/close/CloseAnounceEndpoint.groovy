package pl.poznan.lolx.rest.close

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.poznan.lolx.domain.AnounceSearchService
import pl.poznan.lolx.domain.close.CloseAnounceService
import pl.poznan.lolx.domain.jwt.JwtChecker

@RestController
@Slf4j
class CloseAnounceEndpoint {

    @Autowired
    AnounceSearchService anounceSearchService
    @Autowired
    JwtChecker jwtChecker
    @Autowired
    CloseAnounceService closeAnounceService

    @RequestMapping(value = "/anounces/{anounceId}/close", method = RequestMethod.DELETE)
    ResponseEntity add(
            @RequestHeader(value = "Authorization") authorizationHeader,
            @PathVariable String anounceId) {
        log.info("got close anounce {}", anounceId)

        def anounce = anounceSearchService.getById(anounceId);
        def ownerId = anounce.get().ownerId

        if (!jwtChecker.verify(authorizationHeader, ownerId)) {
            log.warn("rejecting close anounce {} due to authorization error", anounce)
            return new ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        closeAnounceService.close(anounce.get())
        new ResponseEntity(HttpStatus.NO_CONTENT)
    }

}
