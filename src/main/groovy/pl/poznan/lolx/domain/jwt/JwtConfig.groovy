package pl.poznan.lolx.domain.jwt

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader

import java.security.PublicKey

@Configuration
class JwtConfig {

    @Value('${publicKey.dir}')
    String publicKeyDir

    @Autowired
    private ResourceLoader resourceLoader

    @Value('${publicKey.files}')
    private String files

    @Bean
    Map<String, PublicKey> certsMap() {
        files.split(",").collectEntries {
            def fileName = it.substring(0, it.indexOf("."))
            [(fileName): PublicKeyConverter.convert(getCertContent(it))]
        }
    }

    def getCertContent(fileName) {
        resourceLoader.getResource(publicKeyDir + fileName).inputStream.bytes
    }
}
