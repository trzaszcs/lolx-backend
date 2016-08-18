package pl.poznan.lolx.rest

import groovyx.net.http.HttpResponseException
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
    // token generated to sub:666
    def jwtToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJmcm9udGVuZCIsImV4cCI6MTQ3MTU5MzUwOSwiaWF0IjoxNDcxNTA3MTA5LCJzdWIiOiI2NjYifQ.PKAqt5kItiQJrt9jxNq6kMxRswOi9_5YCslpJZvU9WxNvQEdsgIQaqebk6TTsB5Al643Ly74vo_kmutEj1FdrKcoQjoPaJv9trFOfW0VnE_nep5kQWADJ1peepDKOglluAQc_we1tenfqUWgxmxpeBjvxs14MkzPFn87cY2gAuDiXVPtRaTWPJLyz3LKuyT5rIh9Egc4v98XYkNjmQeQQ8Epq-TodbHuJYnHgZRB7IAQbeWMIyQ8AyND0Zxt50C2ym4EHoEY08nyfGTNVRSvKs-fE4W8Cwth3zfIXbQTJi5ACBs6YcylrWyyzmDR0DeL5Vtq6xlhNe9Auc4TZTRw2A"
    def ownerId = "666"

    @Test
    void "should add anouncenemnt"() {
        // given
        def http = getHttpClient();
        def anounce = new AnounceRequestDto(title: "t", description: "desc", state: "wlkp", city: "c", ownerId: ownerId)
        // when
        def response = http.post(path: "/anounces", body: anounce, contentType: 'application/json', headers: ["Authorization": buildBearer(jwtToken)])
        // then
        assert response.status == 201
        assert response.data.id
    }

    @Test
    void "should return unauthorized for wrong jwtToken"() {
        // given
        def http = getHttpClient();
        def anounce = new AnounceRequestDto(title: "t", description: "desc", state: "wlkp", city: "c", ownerId: ownerId)
        try {
            // when
            http.post(path: "/anounces", body: anounce, contentType: 'application/json', headers: ["Authorization": buildBearer("XYZ")])
            assert false
        } catch (HttpResponseException e) {
            assert e.response.status == 401
        }
    }

    RESTClient getHttpClient() {
        new RESTClient("http://localhost:${serverPort}")
    }

    def buildBearer(jwtToken) {
        "Bearer ${jwtToken}"
    }
}
