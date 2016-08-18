package pl.poznan.lolx.domain.jwt

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig {

    @Value('${publicKey.path}')
    String publicKeyPath

    @Autowired
    ApplicationContext context

    @Bean
    JwtChecker jwtChecker() {
        new JwtChecker(context.getResource(publicKeyPath).file.bytes)
    }
}
