package pl.poznan.lolx.rest.requestOrder

import groovyx.net.http.HttpResponseException
import org.junit.Test
import pl.poznan.lolx.domain.AnounceDuration
import pl.poznan.lolx.domain.AnounceType
import pl.poznan.lolx.rest.IntTest
import pl.poznan.lolx.rest.add.AnounceRequestDto
import pl.poznan.lolx.rest.add.LocationDto
import pl.poznan.lolx.util.JwtUtil

class AddRequestOrderTest extends IntTest {

    def anounce = new AnounceRequestDto(
            title: "t",
            description: "desc",
            categoryId: "1",
            location: new LocationDto(title: "Poznan", latitude: 22.3d, longitude: 22.3d),
            ownerId: ownerId,
            price: 22.23,
            type: AnounceType.OFFER,
            duration: AnounceDuration.SEVEN_DAYS,
            contactPhone: "222 333 444")

    def requestOrderOwnerId = "777"
    def requestOrderJwt = JwtUtil.gen(requestOrderOwnerId)
    def requestOrderOwnerBearerToken = "Bearer ${requestOrderJwt}"

    @Test
    void "should create request order"() {
        // given
        mockUsers()
        mockCategories(anounce.categoryId)
        def anounceId = httpCreate(anounce).data.id
        // when
        def response = httpCreateRequestOrder(anounceId)
        // then
        assert response.status == 200
        def requestOrderId = response.data.id
        assert requestOrderId
        def requestOrderHttpResponse = httpGetRequestOrderForUser(anounceId)
        assert requestOrderHttpResponse.status == 200
        assert requestOrderHttpResponse.data.id == requestOrderId
    }

    @Test
    void "should delete request order"() {
        // given
        mockUsers()
        mockCategories(anounce.categoryId)
        def anounceId = httpCreate(anounce).data.id
        def requestOrderId = httpCreateRequestOrder(anounceId).data.id
        // when
        def response = httpDeleteRequestOrder(requestOrderId)
        // then
        assert response.status == 200
        try {
            httpGetRequestOrderForUser(anounceId)
            assert false
        } catch (HttpResponseException ex) {
            assert ex.response.status == 404
        }
    }

    @Test
    void "should be able to accept order"() {
        // given
        mockUsers()
        mockCategories(anounce.categoryId)
        def anounceId = httpCreate(anounce).data.id
        def requestOrderId = httpCreateRequestOrder(anounceId).data.id
        // when
        def response = httpAcceptRequestOrder(requestOrderId)
        // then
        assert response.status == 200
        def requestOrderHttpResponse = httpGetRequestOrderForUser(anounceId)
        assert requestOrderHttpResponse.data.accepted
    }


    def httpCreateRequestOrder(anounceId) {
        httpClient().post(path: "/request-orders", body: new RequestRequestOrderDto(anounceId: anounceId), contentType: 'application/json', headers: ["Authorization": requestOrderOwnerBearerToken])
    }

    def httpGetRequestOrderForUser(anounceId) {
        httpClient().get(path: "/request-orders/anounce/${anounceId}", contentType: 'application/json', headers: ["Authorization": requestOrderOwnerBearerToken])
    }

    def httpDeleteRequestOrder(requestOrderId) {
        httpClient().delete(path: "/request-orders/${requestOrderId}", contentType: 'application/json', headers: ["Authorization": requestOrderOwnerBearerToken])
    }

    def httpAcceptRequestOrder(requestOrderId) {
        httpClient().post(path: "/request-orders/${requestOrderId}/accept", contentType: 'application/json', headers: ["Authorization": bearerToken])
    }

}
