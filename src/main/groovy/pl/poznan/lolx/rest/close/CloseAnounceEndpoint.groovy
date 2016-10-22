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

    @RequestMapping(value = "/anounces/{anounceId}", method = RequestMethod.DELETE)
    ResponseEntity add(
            @RequestHeader(value = "Authorization") authorizationHeader,
            @PathVariable String anounceId) {
        log.info("got close anounce {}", anounceId)
        def anounceOpt = anounceSearchService.getById(anounceId);

        return anounceOpt.map({ anounce ->
            if (!jwtChecker.verify(authorizationHeader, anounce.ownerId)) {
                log.warn("rejecting close anounce {} due to authorization error", anounce)
                return new ResponseEntity(HttpStatus.UNAUTHORIZED)
            }
            closeAnounceService.close(anounce)
            return new ResponseEntity(HttpStatus.NO_CONTENT)
        }).orElseGet({
            return new ResponseEntity().notFound().build()
        })
    }

}
