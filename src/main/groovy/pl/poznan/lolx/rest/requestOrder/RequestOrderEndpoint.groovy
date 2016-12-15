package pl.poznan.lolx.rest.requestOrder

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import pl.poznan.lolx.domain.jwt.JwtChecker
import pl.poznan.lolx.domain.requestOrder.DetailedRequestOrder
import pl.poznan.lolx.domain.requestOrder.RequestOrder
import pl.poznan.lolx.domain.requestOrder.RequestOrderService

@RestController
@Slf4j
class RequestOrderEndpoint {

    @Autowired
    JwtChecker jwtChecker
    @Autowired
    RequestOrderService requestOrderService

    @RequestMapping(value = "/request-orders", method = RequestMethod.POST)
    ResponseEntity order(@RequestHeader(value = "Authorization") authorizationHeader,
                         @RequestBody @Validated RequestRequestOrderDto dto) {
        log.info("new request order {}", dto)
        def id = requestOrderService.requestOrder(dto.anounceId, jwtChecker.subject(authorizationHeader))
        ResponseEntity.ok(
                new RequestOrderIdDto(
                        id: id
                )
        )
    }


    @RequestMapping(value = "/request-orders/{id}", method = RequestMethod.GET)
    ResponseEntity getDetails(@RequestHeader(value = "Authorization") authorizationHeader,
                              @PathVariable("id") String id) {
        if (authorizationHeader != null) {
            def authorId = jwtChecker.subject(authorizationHeader)
            if (authorId) {
                return requestOrderService.getRequestOrder(id, authorId)
                        .map({ ResponseEntity.ok(map(it)) })
                        .orElse(ResponseEntity.notFound().build())
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    @RequestMapping(value = "/request-orders/{id}", method = RequestMethod.DELETE)
    ResponseEntity delete(@RequestHeader(value = "Authorization") authorizationHeader,
                          @PathVariable("id") String id) {
        if (authorizationHeader != null) {
            def authorId = jwtChecker.subject(authorizationHeader)
            if (authorId) {
                requestOrderService.removeRequestOrder(id, authorId)
                return ResponseEntity.ok().build()
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    @RequestMapping(value = "/request-orders/anounce/{anounceId}/author", method = RequestMethod.GET)
    ResponseEntity getForAnounceAuthor(@RequestHeader(value = "Authorization") authorizationHeader,
                                       @PathVariable("anounceId") String anounceId) {
        if (authorizationHeader != null) {
            def authorId = jwtChecker.subject(authorizationHeader)
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


    @RequestMapping(value = "/request-orders/anounce/{anounceId}", method = RequestMethod.GET)
    ResponseEntity getByAnounceId(@RequestHeader(value = "Authorization") authorizationHeader,
                                  @PathVariable("anounceId") String anounceId) {
        if (authorizationHeader != null) {
            def authorId = jwtChecker.subject(authorizationHeader)
            if (authorId) {
                return requestOrderService.getRequestOrderForAnounce(anounceId, authorId)
                        .map({
                    return ResponseEntity.ok(map(it))
                }).orElse(ResponseEntity.notFound().build())
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    @RequestMapping(value = "/request-orders/author", method = RequestMethod.GET)
    ResponseEntity getByAuthorId(@RequestHeader(value = "Authorization") authorizationHeader) {
        if (authorizationHeader != null) {
            def authorId = jwtChecker.subject(authorizationHeader)
            if (authorId) {
                return ResponseEntity.ok(requestOrderService.findByAuthorId(authorId).collect { map(it) })
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    @RequestMapping(value = "/request-orders/user", method = RequestMethod.GET)
    ResponseEntity getForUser(@RequestHeader(value = "Authorization") authorizationHeader) {
        if (authorizationHeader != null) {
            def authorId = jwtChecker.subject(authorizationHeader)
            if (authorId) {
                return ResponseEntity.ok(requestOrderService.findForUser(authorId).collect { map(it) })
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }


    @RequestMapping(value = "/request-orders/anounce-author", method = RequestMethod.GET)
    ResponseEntity getForAnounceAuthor(@RequestHeader(value = "Authorization") authorizationHeader) {
        if (authorizationHeader != null) {
            def authorId = jwtChecker.subject(authorizationHeader)
            if (authorId) {
                return ResponseEntity.ok(requestOrderService.findByAnounceAuthorId(authorId).collect { map(it) })
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    @RequestMapping(value = "/request-orders/{id}/accept", method = RequestMethod.PUT)
    ResponseEntity accept(@RequestHeader(value = "Authorization") authorizationHeader,
                          @PathVariable("id") String id) {
        if (authorizationHeader != null) {
            log.warn("accept order")
            def authorId = jwtChecker.subject(authorizationHeader)
            if (authorId) {
                requestOrderService.accept(id, authorId)
                return ResponseEntity.ok().build()
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    @RequestMapping(value = "/request-orders/{id}/reject", method = RequestMethod.PUT)
    ResponseEntity reject(@RequestHeader(value = "Authorization") authorizationHeader,
                          @PathVariable("id") String id) {
        if (authorizationHeader != null) {
            log.warn("rejecting order")
            def authorId = jwtChecker.subject(authorizationHeader)
            if (authorId) {
                requestOrderService.reject(id, authorId)
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
                status: order.status
        )
    }

    def map(DetailedRequestOrder order) {
        new DetailedRequestOrderDto(
                id: order.requestOrder.id,
                authorId: order.requestOrder.authorId,
                anounceId: order.requestOrder.anounceId,
                creationDate: order.requestOrder.creationDate,
                status: order.requestOrder.status,
                anounceTitle: order.anounceTitle,
                anounceAuthorName: order.anounceAuthorName,
                authorName: order.requestOrderAuthorName
        )
    }

}