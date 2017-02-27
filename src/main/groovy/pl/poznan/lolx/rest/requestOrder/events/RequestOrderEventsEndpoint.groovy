package pl.poznan.lolx.rest.requestOrder.events

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import pl.poznan.lolx.rest.shared.jwt.JwtChecker
import pl.poznan.lolx.domain.requestOrder.events.UnseenRequestOrdersEventsService

@RestController
@Slf4j
class RequestOrderEventsEndpoint {

    @Autowired
    JwtChecker jwtChecker
    @Autowired
    UnseenRequestOrdersEventsService service

    @RequestMapping(value = "/request-orders/events/unseen", method = RequestMethod.GET)
    ResponseEntity unseenEvents(@RequestHeader(value = "Authorization") authorizationHeader) {
        if (authorizationHeader != null) {
            def authorId = jwtChecker.subject(authorizationHeader)
            log.info("get unseen orders for user '$authorId'")
            if (authorId) {
                return ResponseEntity.ok(service.getEvents(authorId).collect { new EventDto(id: it.requestOrderId, type: it.type) })
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

}
