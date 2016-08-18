package pl.poznan.lolx.domain.jwt

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
class JwtConfig {

    @Value('${publicKey.path}')
    String publicKeyPath

    @Bean
    String publicCertPathInClasspath() {
        new ClassPathResource(publicKeyPath).path
    }

    @Bean
    JwtChecker jwtChecker() {
        new JwtChecker(publicCertPathInClasspath())
    }
}
