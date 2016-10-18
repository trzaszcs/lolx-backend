package pl.poznan.lolx.rest

import org.junit.Test

class GetAnounceIntTest extends IntTest {

    @Test
    void "should get anouncenemnt"() {
        assert httpGet("1").data.title != null
    }
}
