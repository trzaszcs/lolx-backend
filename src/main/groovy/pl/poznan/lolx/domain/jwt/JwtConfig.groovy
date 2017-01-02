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

    @Value('${publicKey.files}')
    private String files

    @Bean
    Map<String, PublicKey> certsMap() {
        println("xxx>")
        resourceLoader.getResource("classpath:chat.der").file
        println("xxx>")

        println("------->")
        resourceLoader.getResource("classpath:certs/chat.der").file
        println("------->")


        files.split(',').collectEntries { it ->
            def file = getFile(it)
            [(file.name.substring(0, file.name.indexOf("."))): PublicKeyConverter.convert(file.bytes)]
        }
    }

    def getFile(fileName) {
        resourceLoader.getResource(publicKeyDir + fileName).file
    }
}
