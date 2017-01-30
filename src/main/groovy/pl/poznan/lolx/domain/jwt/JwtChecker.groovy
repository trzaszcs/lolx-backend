package pl.poznan.lolx.domain.jwt

import groovy.util.logging.Slf4j
import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Slf4j
@Component
class JwtChecker {

    private SigningKeyResolver keyResolver

    @Autowired
    JwtChecker(SigningKeyResolver keyResolver) {
        this.keyResolver = keyResolver
    }

    boolean verify(String authorizationHeader, String subject) {
        try {
            def jwtSubject = extractSubject(authorizationHeader)
            if (jwtSubject != subject) {
                log.warn("wrong jwt subject ${jwtSubject} != ${subject}")
                return false
            }
            return true
        } catch (SignatureException e) {
            log.warn("wrong jwt signature", e)
        } catch (ExpiredJwtException e) {
            log.warn("jwt token expired", e)
            throw new JwtExpiredException()
        } catch (MalformedJwtException e) {
            log.warn("malformed jwt token", e)
        } catch (UnsupportedJwtException e) {
            log.warn("unable to check jwt", e)
        } catch (IllegalArgumentException e) {
            log.warn("problem with jwt verify", e)
        }
        return false
    }

    String subject(String authorizationHeader) {
        try {
            def jwtSubject = extractSubject(authorizationHeader)
            return jwtSubject
        } catch (SignatureException e) {
            log.warn("wrong jwt signature", e)
        } catch (ExpiredJwtException e) {
            log.warn("jwt token expired", e)
            throw new JwtExpiredException()
        } catch (MalformedJwtException e) {
            log.warn("malformed jwt token", e)
        } catch (UnsupportedJwtException e) {
            log.warn("unable to check jwt", e)
        }
        return null
    }

    private String extractSubject(String authorizationHeader) {
        return Jwts.parser().setSigningKeyResolver(keyResolver).parseClaimsJws(extractJwt(authorizationHeader)).getBody().getSubject()
    }

    private def extractJwt(authorizationHeader) {
        authorizationHeader.replace('Bearer ', '')
    }
}
