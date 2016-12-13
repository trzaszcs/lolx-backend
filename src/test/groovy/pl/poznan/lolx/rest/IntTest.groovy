package pl.poznan.lolx.rest

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import groovyx.net.http.RESTClient
import org.junit.After
import org.junit.Rule
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import pl.poznan.lolx.AppConfig
import pl.poznan.lolx.infrastructure.AnounceMongoDao
import pl.poznan.lolx.infrastructure.db.AnounceMongoRepository
import pl.poznan.lolx.infrastructure.db.RequestOrderMongoRepository
import pl.poznan.lolx.rest.requestOrder.RequestRequestOrderDto
import pl.poznan.lolx.util.JwtUtil

import static com.github.tomakehurst.wiremock.client.WireMock.*

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [AppConfig])
@ActiveProfiles("test")
abstract class IntTest {

    @LocalServerPort
    protected int serverPort
    @Autowired
    AnounceMongoRepository anounceMongoRepository
    @Autowired
    RequestOrderMongoRepository requestOrderMongoRepository


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

    def mockBulkUsers(usersMap) {
        def json = usersMap.collect { id, name ->
            """
            "$id": {
                "id": "$id",
                "firstName": "$name"
            }
            """
        }.join(",")

        WireMock.stubFor(get(urlMatching("/users/bulk.*"))
                .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""{$json}""")))
    }

    def createAnounce(anounce) {
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
        httpClient().get(path: "/anounces/bulk", query: [id: anounceIds], contentType: 'application/json')
    }

    def httpCreateRequestOrder(anounceId, token) {
        httpClient().post(path: "/request-orders", body: new RequestRequestOrderDto(anounceId: anounceId), contentType: 'application/json', headers: ["Authorization": token])
    }

    def httpGetRequestOrderForUser(anounceId, token) {
        httpClient().get(path: "/request-orders/anounce/${anounceId}", contentType: 'application/json', headers: ["Authorization": token])
    }

    def httpGetRequestOrdersForUser(token) {
        httpClient().get(path: "/request-orders/author", contentType: 'application/json', headers: ["Authorization": token])
    }

    def httpDeleteRequestOrder(requestOrderId, token) {
        httpClient().delete(path: "/request-orders/${requestOrderId}", contentType: 'application/json', headers: ["Authorization": token])
    }

    def httpAcceptRequestOrder(requestOrderId) {
        httpClient().post(path: "/request-orders/${requestOrderId}/accept", contentType: 'application/json', headers: ["Authorization": bearerToken])
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

    @After
    void cleanup() {
        anounceMongoRepository.deleteAll()
        requestOrderMongoRepository.deleteAll()
    }
}
