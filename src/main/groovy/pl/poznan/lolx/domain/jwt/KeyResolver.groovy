package pl.poznan.lolx.domain.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwsHeader
import io.jsonwebtoken.SigningKeyResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.security.Key
import java.security.PublicKey

@Component
class KeyResolver implements SigningKeyResolver {

    private Map<String, PublicKey> keysMap

    @Autowired
    KeyResolver(Map<String, PublicKey> certsMap) {
        this.keysMap = certsMap
    }

    @Override
    Key resolveSigningKey(JwsHeader header, Claims claims) {
        return keysMap.get(claims.get("iss"))
    }

    @Override
    Key resolveSigningKey(JwsHeader header, String plaintext) {
        return null
    }
}
