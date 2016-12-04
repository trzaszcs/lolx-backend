package pl.poznan.lolx.rest.requestOrder

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import pl.poznan.lolx.domain.jwt.JwtChecker
import pl.poznan.lolx.domain.requestOrder.RequestOrder
import pl.poznan.lolx.domain.requestOrder.RequestOrderService

@RestController(value = "request-orders")
@Slf4j
class RequestOrderEndpoint {

    @Autowired
    JwtChecker jwtChecker
    @Autowired
    RequestOrderService requestOrderService

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity order(@RequestHeader(value = "Authorization", required = false) authorizationHeader,
                         @RequestBody @Validated RequestRequestOrderDto dto) {
        log.info("new order {}", dto)

        if (authorizationHeader != null && !jwtChecker.verify(authorizationHeader, dto.authorId)) {
            log.warn("rejecting order {} due to authorization error", dto)
            //TODO if needed
        }

        def id = requestOrderService.requestOrder(dto.anounceId, dto.authorId)
        ResponseEntity.ok(
                new RequestOrderIdDto(
                        id: id
                )
        )
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    ResponseEntity delete(@RequestHeader(value = "Authorization", required = false) authorizationHeader,
                          @PathVariable("id") String id) {
        if (authorizationHeader != null) {
            def authorId = jwtChecker.verify(authorizationHeader)
            if (authorId) {
                requestOrderService.removeRequestOrder(id, authorId)
                return ResponseEntity.ok().build()
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    @RequestMapping(value = "/anounce/{anounceId}/author", method = RequestMethod.GET)
    ResponseEntity getForAnounceAuthor(@RequestHeader(value = "Authorization", required = false) authorizationHeader,
                                       @PathVariable("anounceId") String anounceId) {
        if (authorizationHeader != null) {
            def authorId = jwtChecker.verify(authorizationHeader)
            if (authorId) {
                ResponseEntity.ok {
                    requestOrderService.getRequestOrdersForAnounceAuthor(anounceId, authorId).collect {
                        map(it)
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }


    @RequestMapping(value = "/anounce/{anounceId}", method = RequestMethod.GET)
    ResponseEntity get(@RequestHeader(value = "Authorization", required = false) authorizationHeader,
                       @PathVariable("anounceId") String anounceId) {
        if (authorizationHeader != null) {
            log.warn("rejecting order {} due to authorization error", dto)
            def authorId = jwtChecker.verify(authorizationHeader)
            if (authorId) {
                requestOrderService.getRequestOrderForAnounce(anounceId, authorId)
                        .map {
                    ResponseEntity.ok {
                        map(it)
                    }
                }.orElse {
                    ResponseEntity.notFound().build()
                }
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
    }


    @RequestMapping(value = "/accept/{id}", method = RequestMethod.POST)
    ResponseEntity accept(@RequestHeader(value = "Authorization", required = false) authorizationHeader,
                          @PathVariable("id") String id) {
        if (authorizationHeader != null) {
            log.warn("rejecting order")
            def authorId = jwtChecker.verify(authorizationHeader)
            if (authorId) {
                requestOrderService.acceptOrder(id, authorId)
                return ResponseEntity.ok().build()
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    def map(RequestOrder order) {
        new RequestOrderDto(
                id: order.id,
                authorId: order.authorId,
                anounceId: order.anounceId,
                creationDate: order.creationDate,
                accepted: order.accepted
        )
    }

}
