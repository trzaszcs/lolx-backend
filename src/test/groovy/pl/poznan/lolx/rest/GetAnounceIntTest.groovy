package pl.poznan.lolx.rest

import org.junit.Test

class GetAnounceIntTest extends IntTest {

    @Test
    void "should get anouncenemnt"() {
        assert httpGet("1").data.title != null
    }

    @Test
    void "should bulk get anouncenemnt"() {
        def anouncesMap =  httpBulkGet("1").data
        assert anouncesMap["1"].title
    }
}
