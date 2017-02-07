package pl.poznan.lolx.domain.upload

import org.junit.Test


class ScaledImageNameTest {

    @Test
    void "should return scaled image name"() {
        // given
        def fileName = "name"
        def fileExt = "png"
        assert "$fileName-" + ScaledImageSize.MEDIUM.name() + ".$fileExt" == ScaledImageSize.MEDIUM.getFileName("$fileName.$fileExt")
    }

}
