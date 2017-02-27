package pl.poznan.lolx.infrastructure.notification

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import pl.poznan.lolx.domain.notification.NotificationClient
import pl.poznan.lolx.infrastructure.shared.AuthorizationHeaderBuilder

@Slf4j
@Component
class NotificationHttpClient implements NotificationClient {

    @Value('${notification-service.addr}')
    String serviceAddress

    @Autowired
    RestTemplate restTemplate

    @Autowired
    AuthorizationHeaderBuilder authorizationHeaderBuilder

    @Override
    void requestCreated(String to, String requestOrderUrl, String anounceTitle) {
        send(to, NotificationType.ORDER_CREATED, requestOrderUrl, anounceTitle)
    }

    @Override
    void requestAccepted(String to, String requestOrderUrl, String anounceTitle) {
        send(to, NotificationType.ORDER_ACCEPTED, requestOrderUrl, anounceTitle)
    }

    @Override
    void requestRejected(String to, String requestOrderUrl, String anounceTitle) {
        send(to, NotificationType.ORDER_REJECTED, requestOrderUrl, anounceTitle)
    }

    def send(String to, NotificationType type, String requestOrderUrl, String anounceTitle) {
        log.info("sending {} notification to {}", type, to)
        def headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON)
        headers.set("Authorization", authorizationHeaderBuilder.build(to))
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON))
        def entity = new HttpEntity<NotificationDto>(
                request(to, type, requestOrderUrl, anounceTitle),
                headers
        )
        restTemplate.exchange(
                "${serviceAddress}notify",
                HttpMethod.POST,
                entity,
                String.class)

    }

    def request(String email, NotificationType type, String requestOrderUrl, String anounceTitle) {
        new NotificationDto(
                email: email,
                type: type.getType(),
                context: new RequestOrdetContextDto(url: requestOrderUrl, anounceTitle: anounceTitle)
        )
    }
}
