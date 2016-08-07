package pl.poznan.lolx.rest.add

import groovy.util.logging.Slf4j
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@Slf4j
class AddAnounceEndpoint {

    @RequestMapping(value = "/anounces", method = RequestMethod.POST)
    void add(@RequestBody AnounceRequestDto anounceDto) {
        log.info("got new anounce {}", anounceDto)
    }

}
