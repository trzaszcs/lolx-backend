package pl.poznan.lolx.infrastructure.shared

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader

import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec

@Configuration
class JwtBuilderConfig {

    @Autowired
    private ResourceLoader resourceLoader

    @Value('${private-key.path}')
    String privateKeyPath

    @Bean
    PrivateKey privateKey() {
        def byteArray = resourceLoader.getResource(privateKeyPath).inputStream.bytes
        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(byteArray)
        def kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

}
