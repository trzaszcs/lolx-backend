package pl.poznan.lolx.domain.jwt

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader

import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec

@Configuration
class JwtConfig {

    @Value('${publicKey.dir}')
    String publicKeyDir

    @Autowired
    private ResourceLoader resourceLoader

    @Bean
    Map<String, PublicKey> certsMap() {
        return resourceLoader.getResource(publicKeyDir).file.listFiles().collectEntries {
            [(it.name.substring(0, it.name.indexOf("."))): PublicKeyConverter.convert(it.bytes)]
        }
    }
}
