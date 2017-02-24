package pl.poznan.lolx.rest.shared.jwt

import org.junit.Before
import org.junit.Test
import pl.poznan.lolx.util.JwtUtil

class JwtCheckerTest {

    def jwtChecker
    def jwtToken = JwtUtil.gen("666")

    @Before
    void setup() {
        jwtChecker = new JwtChecker(new KeyResolver([
                "test": PublicKeyConverter.convert(getClass().getClassLoader().getResource("tst_public.der").getBytes())]))
    }

    @Test
    void "should return true for correct subject"() {
        assert jwtChecker.verify("Bearer ${jwtToken}", "666")
    }

    @Test
    void "should return false for wrong subject"() {
        assert !jwtChecker.verify("Bearer ${jwtToken}", "wrong subject")
    }

    @Test
    void "should return false for wrong Bearer"() {
        assert !jwtChecker.verify("Bearer xyz", "wrong subject")
    }

    @Test
    void "should return false for wrong Token"() {
        def token = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJKb2UifQ.yiV1GWDrQyCeoOswYTf_xvlgsnaVVYJM0mU6rkmRBf2T1MBl3Xh2kZii0Q9BdX5-G0j25Qv2WF4lA6jPl5GKuA'
        assert !jwtChecker.verify("Bearer ${token}", "666")
    }
}
