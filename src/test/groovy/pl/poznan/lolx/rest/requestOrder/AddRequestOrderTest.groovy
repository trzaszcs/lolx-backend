package pl.poznan.lolx.rest.requestOrder

import groovyx.net.http.HttpResponseException
import org.junit.Test
import pl.poznan.lolx.domain.AnounceDuration
import pl.poznan.lolx.domain.requestOrder.Status
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
            duration: AnounceDuration.SEVEN_DAYS)

    def requestOrderOwnerId = "777"
    def requestOrderJwt = JwtUtil.gen(requestOrderOwnerId)
    def requestOrderOwnerBearerToken = "Bearer ${requestOrderJwt}"

    @Test
    void "should create request order"() {
        // given
        mockUsers()
        mockBulkUsers([(ownerId): "A", (requestOrderOwnerId): "B"])
        def anounceId = httpCreate(anounce).data.id
        // when
        def response = httpCreateRequestOrder(anounceId, requestOrderOwnerBearerToken)
        // then
        assert response.status == 200
        def requestOrderId = response.data.id
        assert requestOrderId
        def requestOrderHttpResponse = httpGetRequestOrderForUser(anounceId, requestOrderOwnerBearerToken)
        assert requestOrderHttpResponse.status == 200
        assert requestOrderHttpResponse.data.id == requestOrderId
    }

    @Test
    void "should delete request order"() {
        // given
        mockUsers()
        def anounceId = httpCreate(anounce).data.id
        def requestOrderId = httpCreateRequestOrder(anounceId, requestOrderOwnerBearerToken).data.id
        // when
        def response = httpDeleteRequestOrder(requestOrderId, requestOrderOwnerBearerToken)
        // then
        assert response.status == 200
        try {
            httpGetRequestOrderForUser(anounceId, requestOrderOwnerBearerToken)
            assert false
        } catch (HttpResponseException ex) {
            assert ex.response.status == 404
        }
    }

    @Test
    void "should be able to accept order"() {
        // given
        mockUsers()
        mockBulkUsers([(ownerId): "A", (requestOrderOwnerId): "B"])
        def anounceId = httpCreate(anounce).data.id
        def requestOrderId = httpCreateRequestOrder(anounceId, requestOrderOwnerBearerToken).data.id
        // when
        def response = httpAcceptRequestOrder(requestOrderId)
        // then
        assert response.status == 200
        def requestOrderHttpResponse = httpGetRequestOrderForUser(anounceId, requestOrderOwnerBearerToken)
        assert requestOrderHttpResponse.data.status == Status.ACCEPTED.name()
    }

}
