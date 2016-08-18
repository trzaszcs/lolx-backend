package pl.poznan.lolx.domain.jwt

import groovy.util.logging.Slf4j
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException

import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec

@Slf4j
class JwtChecker {

    private PublicKey publicKey

    JwtChecker(byte[] publicKeyContent) {
        assert publicKeyContent != null
        this.publicKey = toPublicKey(publicKeyContent)
    }

    boolean verify(String authorizationHeader, String subject) {
        try {
            def jwtSubject = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(extractJwt(authorizationHeader)).getBody().getSubject()
            if (jwtSubject != subject) {
                log.warn("wrong jwt subject ${jwtSubject} != ${subject}")
                return false
            }
            return true
        } catch (SignatureException e) {
            log.warn("wrong jwt signature", e)
        } catch (MalformedJwtException e) {
            log.warn("malformed jwt token", e)
        } catch (UnsupportedJwtException e) {
            log.warn("unable to check jwt", e)
        }
        return false
    }

    private def extractJwt(authorizationHeader) {
        authorizationHeader.replace('Bearer ', '')
    }

    private def toPublicKey(base64Content) {
        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(base64Content);
        def kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}
