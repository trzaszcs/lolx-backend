package pl.poznan.lolx.rest

import groovyx.net.http.HttpResponseException
import org.junit.Test
import pl.poznan.lolx.domain.AnounceDuration
import pl.poznan.lolx.domain.AnounceType
import pl.poznan.lolx.rest.add.AnounceRequestDto
import pl.poznan.lolx.rest.add.LocationDto

class CloseAnounceIntTest extends IntTest {

    def anounce = new AnounceRequestDto(
            title: "t",
            description: "desc",
            categoryId: "1",
            location: new LocationDto(title: "Poznan", latitude: 22.3d, longitude: 22.3d),
            ownerId: ownerId,
            price: 22.23,
            type: AnounceType.OFFER,
            duration: AnounceDuration.SEVEN_DAYS)

    @Test
    void "should close anouncenemnt"() {
        // given
        def http = httpClient()
        def response = createAnounce(anounce)
        assert response.status == 201
        def id = response.data.id
        assert id
        // when
        response = http.delete(path: "/anounces/${id}/close", contentType: 'application/json', headers: ["Authorization": bearerToken])
        // then
        assert response.status == 204
        try {
            httpGet(id)
            assert false
        } catch (HttpResponseException ex) {
            assert ex.statusCode == 404
        }

    }
}
