package pl.poznan.lolx.domain.notification

import groovy.transform.PackageScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@PackageScope
@Component
class RequestOrderUrlBuilder {
    String frontServiceAddr

    @Autowired
    RequestOrderUrlBuilder(@Value('${front-service.addr}') String frontServiceAddr) {
        this.frontServiceAddr = frontServiceAddr
    }

    String build(String id) {
        "$frontServiceAddr/#!/chat?chatId=$id"
    }
}
