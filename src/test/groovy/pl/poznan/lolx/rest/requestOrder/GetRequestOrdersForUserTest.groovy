package pl.poznan.lolx.rest.requestOrder

import org.junit.Test
import pl.poznan.lolx.domain.AnounceDuration
import pl.poznan.lolx.domain.AnounceType
import pl.poznan.lolx.rest.IntTest
import pl.poznan.lolx.rest.add.AnounceRequestDto
import pl.poznan.lolx.rest.add.LocationDto
import pl.poznan.lolx.util.JwtUtil

class GetRequestOrdersForUserTest extends IntTest {

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
    void "should get request orders by author"() {
        // given
        mockUsers()
        def anounceAuthorName = "hyzio"
        def requestOrderAuthorName = "dyzio"
        mockBulkUsers([(ownerId): anounceAuthorName, (requestOrderOwnerId): requestOrderAuthorName])
        mockCategories(anounce.categoryId)
        def anounceId = httpCreate(anounce).data.id
        def requestOrderId = httpCreateRequestOrder(anounceId, requestOrderOwnerBearerToken).data.id
        // when
        def response = httpGetRequestOrdersForUser(requestOrderOwnerBearerToken)
        // then
        assert response.status == 200
        def requestOrders = response.data
        assert requestOrders.size() == 1
        assert requestOrders.get(0).id == requestOrderId
        assert requestOrders.get(0).authorName == requestOrderAuthorName
        assert requestOrders.get(0).anounceAuthorName == anounceAuthorName
    }
}
