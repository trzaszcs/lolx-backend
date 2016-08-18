package pl.poznan.lolx.domain.jwt

import org.junit.Before
import org.junit.Test

class JwtCheckerTest {

    def jwtChecker
    def jwtToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJmcm9udGVuZCIsImV4cCI6MTQ3MTU5MzUwOSwiaWF0IjoxNDcxNTA3MTA5LCJzdWIiOiI2NjYifQ.PKAqt5kItiQJrt9jxNq6kMxRswOi9_5YCslpJZvU9WxNvQEdsgIQaqebk6TTsB5Al643Ly74vo_kmutEj1FdrKcoQjoPaJv9trFOfW0VnE_nep5kQWADJ1peepDKOglluAQc_we1tenfqUWgxmxpeBjvxs14MkzPFn87cY2gAuDiXVPtRaTWPJLyz3LKuyT5rIh9Egc4v98XYkNjmQeQQ8Epq-TodbHuJYnHgZRB7IAQbeWMIyQ8AyND0Zxt50C2ym4EHoEY08nyfGTNVRSvKs-fE4W8Cwth3zfIXbQTJi5ACBs6YcylrWyyzmDR0DeL5Vtq6xlhNe9Auc4TZTRw2A"

    @Before
    void setup() {
        jwtChecker = new JwtChecker(getClass().getClassLoader().getResource("public.der").getBytes())
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
