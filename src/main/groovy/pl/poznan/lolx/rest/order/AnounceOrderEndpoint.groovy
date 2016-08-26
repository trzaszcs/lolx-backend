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
    ResponseEntity order(@RequestHeader(value = "Authorization", required = false) authorizationHeader,
                         @RequestBody @Validated AnounceOrderRequestDto dto) {
        log.info("new order {}", dto)

        if (authorizationHeader != null && !jwtChecker.verify(authorizationHeader, dto.ownerId)) {
            log.warn("rejecting order {} due to authorization error", dto)
            //TODO if needed
        }

        def orderId = anounceOrderService.order(map(dto))
        new ResponseEntity(
                new AnounceOrderStatusDto(
                        requestId: orderId,
                        anounceContactInfo: getAnounceContactInfo(),
                        status: "PENDING"
                ),
                HttpStatus.ACCEPTED
        )
    }

    @RequestMapping(value = "/orders/{requestId}", method = RequestMethod.GET)
    ResponseEntity get(@RequestHeader(value = "Authorization", required = false) authorizationHeader,
                         @PathVariable String requestId) {
        log.info("get order {}", requestId)

        AnounceOrderRequest order = anounceOrderService.get(requestId);
        if (authorizationHeader != null && !jwtChecker.verify(authorizationHeader, order.customerId)) {
            log.warn("rejecting get order {} due to authorization error", requestId)
            //TODO if needed
        }

        new ResponseEntity(
                new AnounceOrderRequestDto(
                        requestId: order.requestId,
                        anounceId: order.anounceId,
                        preferedTime: order.preferedTime,
                        preferedDate: order.preferedDate,
                        customerContactInfo: order.customerContactInfo,
                        status: new AnounceOrderStatusDto(
                                requestId: order.requestId,
                                requestDate: order.requestDate,
                                anounceContactInfo: "informacje kontaktowe do ogłoszenia: tel 600700800",
                                status: "SUCCESS"
                        )
                ),
                HttpStatus.OK
        )
    }

    private static String getAnounceContactInfo() {
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
