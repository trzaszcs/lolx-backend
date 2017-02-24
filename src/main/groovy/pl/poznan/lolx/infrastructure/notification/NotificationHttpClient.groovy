package pl.poznan.lolx.infrastructure.notification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import pl.poznan.lolx.domain.notification.NotificationClient
import pl.poznan.lolx.infrastructure.shared.AuthorizationHeaderBuilder

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
        send(to, "request-created", requestOrderUrl, anounceTitle)
    }

    @Override
    void requestAccepted(String to, String requestOrderUrl, String anounceTitle) {
        send(to, "request-accepted", requestOrderUrl, anounceTitle)
    }

    @Override
    void requestRejected(String to, String requestOrderUrl, String anounceTitle) {
        send(to, "request-rejected", requestOrderUrl, anounceTitle)
    }

    def send(String to, String type, String requestOrderUrl, String anounceTitle) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeaderBuilder.build(to))
        HttpEntity<NotificationDto> entity = new HttpEntity<NotificationDto>(request(to, type, requestOrderUrl, anounceTitle), headers);
        def response = restTemplate.exchange(
                "${serviceAddress}/notif",
                HttpMethod.POST,
                entity,
                String)
    }

    def request(String email, String type, String requestOrderUrl, String anounceTitle) {
        new NotificationDto(
                email: email,
                type: type,
                context: new RequestOrdetContextDto(url: requestOrderUrl, anounceTitle: anounceTitle)
        )
    }
}
