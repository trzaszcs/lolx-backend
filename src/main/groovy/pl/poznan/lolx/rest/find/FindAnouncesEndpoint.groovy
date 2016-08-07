package pl.poznan.lolx.rest.find

import groovy.util.logging.Slf4j
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.poznan.lolx.rest.add.AnounceRequestDto

@RestController
@Slf4j
class FindAnouncesEndpoint {

    @RequestMapping(value = "/anounces/find", method = RequestMethod.GET)
    List<AnounceRequestDto> find(@RequestParam("query") String query) {
        log.info("find anounces by query: {}", query)
        return [
                new SimpleAnounceDto(id: "1", title: "title1", description: "description1...", state: "wlkp", city: "Poz"),
                new SimpleAnounceDto(id: "2", title: "title1", description: "description2...", state: "wlkp", city: "Waw"),
                new SimpleAnounceDto(id: "3", title: "title1", description: "description3...", state: "wlkp", city: "Krk"),
        ]
    }

}
