package pl.poznan.lolx.rest

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import groovyx.net.http.RESTClient
import org.junit.Rule
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import pl.poznan.lolx.AppConfig
import pl.poznan.lolx.util.JwtUtil

import static com.github.tomakehurst.wiremock.client.WireMock.*

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [AppConfig])
@ActiveProfiles("test")
abstract class IntTest {

    @LocalServerPort
    protected int serverPort

    def ownerId = "666"
    def jwtToken = JwtUtil.gen(ownerId)
    def bearerToken = "Bearer ${jwtToken}"

    private static def httpClientInstnance

    @Rule
    public WireMockRule rule = new WireMockRule(12346)

    def mockCategories(categoryId) {
        WireMock.stubFor(get(urlEqualTo("/categories/${categoryId}"))
                .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""{"id": "${categoryId}", "name" : "CATEGORY"}""")))
    }

    def mockUsers() {
        WireMock.stubFor(get(urlEqualTo("/users/${ownerId}"))
                .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""{"id": "${ownerId}", "email" : "email@wp.pl"}""")))
    }

    def createAnounce(anounce){
        mockCategories(anounce.categoryId)
        mockUsers()
        httpCreate(anounce)
    }

    def httpCreate(anounce) {
        httpClient().post(path: "/anounces", body: anounce, contentType: 'application/json', headers: ["Authorization": bearerToken])
    }

    def httpGet(anounceId) {
        httpClient().get(path: "/anounces/$anounceId", contentType: 'application/json')
    }

    def httpBulkGet(anounceIds) {
        httpClient().get(path: "/anounces/bulk", query : [id: anounceIds], contentType: 'application/json')
    }

    RESTClient httpClient() {
        if (!httpClientInstnance) {
            httpClientInstnance = new RESTClient("http://localhost:${serverPort}")
        }
        return httpClientInstnance
    }

    def buildBearer(jwtToken) {
        "Bearer ${jwtToken}"
    }
}
