package pl.poznan.lolx.domain.jwt

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader

@Configuration
class JwtConfig {

    @Value('${publicKey.path}')
    String publicKeyPath

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    JwtChecker jwtChecker() {
        new JwtChecker(resourceLoader.getResource(publicKeyPath).inputStream.bytes)
    }
}
