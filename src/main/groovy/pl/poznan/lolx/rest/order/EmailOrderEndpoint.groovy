package pl.poznan.lolx.rest.order

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import pl.poznan.lolx.domain.AnounceSearchService
import pl.poznan.lolx.domain.jwt.JwtChecker
import pl.poznan.lolx.domain.order.AnounceOrderService

@RestController
@Slf4j
class EmailOrderEndpoint {

    AnounceSearchService anounceSearchService
    AnounceOrderService anounceOrderService
    JwtChecker jwtChecker

    @Autowired
    EmailOrderEndpoint(
            AnounceSearchService anounceSearchService,
            AnounceOrderService anounceOrderService,
            JwtChecker jwtChecker) {
        this.anounceSearchService = anounceSearchService
        this.anounceOrderService = anounceOrderService
        this.jwtChecker = jwtChecker
    }

    @RequestMapping(value = "/orders/email", method = RequestMethod.POST)
    ResponseEntity emailOrder(@RequestHeader(value = "Authorization", required = true) authorizationHeader,
                         @RequestBody @Validated EmailOrderDto dto) {
        log.info("new email order {}", dto)

        def order = anounceOrderService.get(dto.orderId)
        def ownerId = order.customerId

        if (!jwtChecker.verify(authorizationHeader, ownerId)) {
            log.warn("rejecting order {} due to authorization error", dto)
            return new ResponseEntity(
                    HttpStatus.UNAUTHORIZED
            )
        }

        new ResponseEntity(
                HttpStatus.ACCEPTED
        )
    }

}
