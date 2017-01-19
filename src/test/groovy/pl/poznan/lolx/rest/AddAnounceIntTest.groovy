package pl.poznan.lolx.rest

import groovyx.net.http.HttpResponseException
import org.junit.Test
import pl.poznan.lolx.domain.AnounceDuration
import pl.poznan.lolx.domain.AnounceType
import pl.poznan.lolx.rest.add.AnounceRequestDto
import pl.poznan.lolx.rest.add.LocationDto

class AddAnounceIntTest extends IntTest {

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


    @Test
    void "should add anouncenemnt"() {
        // given
        mockUsers()
        mockCategories(anounce.categoryId)
        // when
        def response = httpCreate(anounce)
        // then
        assert response.status == 201
        assert response.data.id
    }

    @Test
    void "should return unauthorized for wrong jwtToken"() {
        try {
            // when
            httpClient().post(path: "/anounces", body: anounce, contentType: 'application/json', headers: ["Authorization": buildBearer("XYZ")])
            assert false
        } catch (HttpResponseException e) {
            assert e.response.status == 401
        }
    }

    def buildBearer(jwtToken) {
        "Bearer ${jwtToken}"
    }
}
