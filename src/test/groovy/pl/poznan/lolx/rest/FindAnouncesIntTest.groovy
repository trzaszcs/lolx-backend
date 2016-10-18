package pl.poznan.lolx.rest

import org.junit.Test

class FindAnouncesIntTest extends IntTest {


    @Test
    void "should find anouncenemnts"() {
        // given
        def queryString = "dummy"
        // when
        def response = httpClient().get(path: "/anounces", query: ['query': queryString])
        // then
        assert response.status == 200
    }

}
