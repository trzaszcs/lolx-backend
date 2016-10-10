package pl.poznan.lolx.rest

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import pl.poznan.lolx.AppConfig
import pl.poznan.lolx.domain.AnounceType
import pl.poznan.lolx.rest.add.AnounceRequestDto
import pl.poznan.lolx.rest.add.LocationDto
import pl.poznan.lolx.util.JwtUtil

import static com.github.tomakehurst.wiremock.client.WireMock.*

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [AppConfig])
@ActiveProfiles("test")
class AddAnounceIntTest {

    @LocalServerPort
    int serverPort

    def ownerId = "666"
    def jwtToken = JwtUtil.gen(ownerId)

    @Rule
    public WireMockRule rule = new WireMockRule(12346)

    def anounce = new AnounceRequestDto(
            title: "t",
            description: "desc",
            categoryId: "1",
            location: new LocationDto(title: "Poznan", latitude: 22.3d, longitude: 22.3d),
            ownerId: ownerId,
            price: 22.23,
            type: AnounceType.OFFER)


    @Test
    void "should add anouncenemnt"() {
        // given
        def http = getHttpClient();
        WireMock.stubFor(get(urlEqualTo("/users/${ownerId}"))
                .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""{"id": "${ownerId}", "email" : "email@wp.pl"}""")))
        WireMock.stubFor(get(urlEqualTo("/categories/${anounce.categoryId}"))
                .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""{"id": "${anounce.categoryId}", "name" : "CATEGORY"}""")))
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
