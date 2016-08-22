package pl.poznan.lolx.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm

import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec

final class JwtUtil {

    static String gen(subject) {
        def bytes = JwtUtil.class.classLoader.getResourceAsStream("private_key.der").bytes
        def key = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(bytes))
        Jwts.builder().claim("sub", subject)
                .signWith(SignatureAlgorithm.RS256, key)
                .compact()
    }


}
