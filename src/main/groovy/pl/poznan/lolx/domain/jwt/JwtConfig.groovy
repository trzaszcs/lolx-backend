package pl.poznan.lolx.domain.jwt

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.ResourcePatternResolver

import java.security.PublicKey

@Configuration
class JwtConfig {

    @Value('${publicKey.dir}')
    String publicKeyDir

    @Autowired
    private ResourceLoader resourceLoader

    @Autowired
    private ResourcePatternResolver resolver

    @Bean
    Map<String, PublicKey> certsMap() {
        resolver.getResources(publicKeyDir+"*.*").collectEntries {
            def file = it.file
            [(file.name.substring(0, file.name.indexOf("."))): PublicKeyConverter.convert(file.bytes)]
        }
    }
}
