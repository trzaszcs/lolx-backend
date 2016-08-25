package pl.poznan.lolx.rest.order

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import pl.poznan.lolx.domain.jwt.JwtChecker
import pl.poznan.lolx.domain.order.AnounceOrderRequest
import pl.poznan.lolx.domain.order.AnounceOrderService

@RestController
@Slf4j
class AnounceOrderEndpoint {

    AnounceOrderService anounceOrderService
    JwtChecker jwtChecker

    @Autowired
    AnounceOrderEndpoint(
            AnounceOrderService anounceOrderService,
            JwtChecker jwtChecker) {
        this.anounceOrderService = anounceOrderService
        this.jwtChecker = jwtChecker
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    ResponseEntity add(@RequestHeader(value = "Authorization") authorizationHeader,
                       @RequestBody @Validated AnounceOrderRequestDto dto) {
        log.info("got new order {}", dto)

        if (!jwtChecker.verify(authorizationHeader, dto.ownerId)) {
            log.warn("rejecting anounce {} due to authorization error", dto)
            //TODO if needed
        }

        def orderId = anounceOrderService.order(map(dto))
        new ResponseEntity(
                new AnounceOrderRequestDto(requestId: orderId, customerContactInfo: getAnounceContactInfo()),
                HttpStatus.ACCEPTED)
    }

    private String getAnounceContactInfo() {
        "dzwon 600700800"
    }

    def map(AnounceOrderRequestDto anounceOrderRequestDto) {
        new AnounceOrderRequest(
            requestId: anounceOrderRequestDto.requestId,
            anounceId: anounceOrderRequestDto.anounceId,
            preferedTime: anounceOrderRequestDto.preferedTime,
            preferedDate: anounceOrderRequestDto.preferedDate,
            customerContactInfo: anounceOrderRequestDto.customerContactInfo
        )
    }

}
