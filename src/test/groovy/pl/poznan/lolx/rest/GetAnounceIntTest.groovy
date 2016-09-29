package pl.poznan.lolx.rest

import com.github.tomakehurst.wiremock.junit.WireMockRule
import groovyx.net.http.RESTClient
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import pl.poznan.lolx.AppConfig
import pl.poznan.lolx.util.JwtUtil

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [AppConfig])
@ActiveProfiles("test")
class GetAnounceIntTest {

    @LocalServerPort
    int serverPort

    @Test
    void "should get anouncenemnt"() {
        def http = getHttpClient()
        assert http.get(path: "/anounces/1", contentType: 'application/json').data.title != null
    }

    RESTClient getHttpClient() {
        new RESTClient("http://localhost:${serverPort}")
    }

}
