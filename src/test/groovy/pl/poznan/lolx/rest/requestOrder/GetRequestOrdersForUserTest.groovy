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
            type: AnounceType.ORDER,
            duration: AnounceDuration.SEVEN_DAYS)

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
        def searchResult = response.data
        assert searchResult.totalCount == 1
        assert searchResult.requestOrders.size() == 1
        assert searchResult.requestOrders.get(0).id == requestOrderId
        assert searchResult.requestOrders.get(0).authorName == requestOrderAuthorName
        assert searchResult.requestOrders.get(0).anounceAuthorName == anounceAuthorName
    }

    @Test
    void "should return request orders by specific status"() {
        // given
        mockUsers()
        def anounceAuthorName = "hyzio"
        def requestOrderAuthorName = "dyzio"
        mockBulkUsers([(ownerId): anounceAuthorName, (requestOrderOwnerId): requestOrderAuthorName])
        mockCategories(anounce.categoryId)

        // create 2 anounces

        def anounceId1 = httpCreate(anounce).data.id
        def anounceId2 = httpCreate(anounce).data.id

        // create 2 request orders

        def requestOrderId1 = httpCreateRequestOrder(anounceId1, requestOrderOwnerBearerToken).data.id
        def requestOrderId2 = httpCreateRequestOrder(anounceId2, requestOrderOwnerBearerToken).data.id

        // accept one

        httpAcceptRequestOrder(requestOrderId1)

        // when
        def response = httpGetRequestOrdersForUser(requestOrderOwnerBearerToken, StatusDto.ACCEPTED)
        // then
        assert response.status == 200
        def searchResult = response.data
        assert searchResult.totalCount == 1
        assert searchResult.requestOrders.size() == 1
        assert searchResult.requestOrders.get(0).id == requestOrderId1
    }
}
