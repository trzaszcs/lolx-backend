package pl.poznan.lolx.infrastructure.add

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class ExternalResourcesConfig {

    @Bean
    RestTemplate restTemplate(){
        new RestTemplate()
    }

}
