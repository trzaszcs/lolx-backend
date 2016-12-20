package pl.poznan.lolx.domain.jwt

import groovy.transform.PackageScope

import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec

@PackageScope
class PublicKeyConverter {

    static PublicKey convert(byte[] byteArray) {
        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(byteArray)
        def kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

}
