package pl.poznan.lolx.rest

import groovyx.net.http.RESTClient
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import pl.poznan.lolx.AppConfig
import pl.poznan.lolx.rest.add.AnounceRequestDto

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [AppConfig])
class AddAnounceIntTest {

    @LocalServerPort
    int serverPort

    @Test
    void "should add anouncenemnt"() {
        // given
        def http = getHttpClient();
        def anounce = new AnounceRequestDto(title: "t", description: "desc", state: "wlkp", city: "c", ownerId: "ownerId")
        // when
        def response = http.post(path: "/anounces", body: anounce, contentType: 'application/json')
        // then
        assert response.status == 200
        assert response.data.id
    }

    RESTClient getHttpClient() {
        new RESTClient("http://localhost:${serverPort}")
    }
}
