package pl.poznan.lolx.rest.requestOrder.events

import org.junit.Test
import pl.poznan.lolx.domain.AnounceDuration
import pl.poznan.lolx.domain.AnounceType
import pl.poznan.lolx.domain.requestOrder.events.EventType
import pl.poznan.lolx.rest.IntTest
import pl.poznan.lolx.rest.add.AnounceRequestDto
import pl.poznan.lolx.rest.add.LocationDto
import pl.poznan.lolx.util.JwtUtil

class RequestOrderEventsIntTest extends IntTest {

    def anounce = new AnounceRequestDto(
            title: "t",
            description: "desc",
            categoryId: "1",
            location: new LocationDto(title: "Poznan", latitude: 22.3d, longitude: 22.3d),
            ownerId: ownerId,
            price: 22.23,
            type: AnounceType.ORDER,
            duration: AnounceDuration.SEVEN_DAYS,
            contactPhone: "222 333 444")

    def requestOrderOwnerId = "777"
    def requestOrderJwt = JwtUtil.gen(requestOrderOwnerId)
    def requestOrderOwnerBearerToken = "Bearer ${requestOrderJwt}"

    @Test
    void "should receive new request order event"() {
        // given
        mockUsers()
        mockBulkUsers([(ownerId): "A", (requestOrderOwnerId): "B"])
        mockCategories(anounce.categoryId)
        def anounceId = httpCreate(anounce).data.id
        def requestOrderId = httpCreateRequestOrder(anounceId, requestOrderOwnerBearerToken).data.id
        // when
        def response = httpUnseenEvents()
        // then
        assert response.status == 200
        def events = response.data
        assert events.size() == 1
        def createEvent = events[0]
        assert createEvent.id == requestOrderId
        assert createEvent.type == EventType.NEW_REQUEST_ORDER.name()
    }

    def httpUnseenEvents() {
        httpClient().get(path: "/request-orders/events/unseen", contentType: 'application/json', headers: ["Authorization": bearerToken])
    }

}
