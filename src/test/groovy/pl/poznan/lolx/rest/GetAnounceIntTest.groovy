package pl.poznan.lolx.rest

import org.junit.Test
import pl.poznan.lolx.domain.AnounceDuration
import pl.poznan.lolx.rest.add.AnounceRequestDto
import pl.poznan.lolx.rest.add.LocationDto

class GetAnounceIntTest extends IntTest {

    def anounce = new AnounceRequestDto(
            title: "title",
            description: "desc",
            categoryId: "1",
            location: new LocationDto(title: "Poznan", latitude: 22.3d, longitude: 22.3d),
            ownerId: ownerId,
            price: 22.23,
            duration: AnounceDuration.SEVEN_DAYS)

    @Test
    void "should get anouncenemnt"() {
        mockUsers()
        def anounceId = httpCreate(anounce).data.id
        assert httpGet(anounceId).data.title == anounce.title
    }

    @Test
    void "should bulk get anouncenemnt"() {
        mockUsers()
        def anounceId = httpCreate(anounce).data.id
        def anouncesMap =  httpBulkGet(anounceId).data
        assert anouncesMap[anounceId].title
    }
}
