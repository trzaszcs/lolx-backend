package pl.poznan.lolx.infrastructure.shared

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AuthorizationHeaderBuilder {

    @Autowired
    JwtBuilder jwtBuilder

    String build(String subject) {
        return "Bearer ${jwtBuilder.build(subject)}"
    }
}
