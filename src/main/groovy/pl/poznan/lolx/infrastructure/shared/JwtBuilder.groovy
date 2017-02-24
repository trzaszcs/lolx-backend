package pl.poznan.lolx.infrastructure.shared

import groovy.transform.PackageScope
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.security.PrivateKey

@PackageScope
@Component
class JwtBuilder {

    @Autowired
    PrivateKey privateKey

    String build(String subject) {
        return Jwts
                .builder()
                .setSubject(subject).setIssuer("backend").signWith(SignatureAlgorithm.HS512, privateKey).compact()
    }
}
