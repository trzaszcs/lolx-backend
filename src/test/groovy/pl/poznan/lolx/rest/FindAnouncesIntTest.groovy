package pl.poznan.lolx.rest

import groovyx.net.http.RESTClient
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import pl.poznan.lolx.AppConfig

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [AppConfig])
class FindAnouncesIntTest {

    @LocalServerPort
    int serverPort


    @Test
    void "should find anouncenemnts"() {
        // given
        def http = getHttpClient();
        def queryString = "dummy"
        // when
        def response = http.get(path: "/anounces", query: ['query': queryString])
        // then
        assert response.status == 200
    }

    RESTClient getHttpClient() {
        new RESTClient("http://localhost:${serverPort}")
    }
}
