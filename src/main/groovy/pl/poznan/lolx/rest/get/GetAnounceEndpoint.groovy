package pl.poznan.lolx.rest.get

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import pl.poznan.lolx.domain.AnounceSearchService
import pl.poznan.lolx.rest.add.LocationDto

@RestController
@Slf4j
class GetAnounceEndpoint {

    AnounceSearchService anounceSearchService

    @Autowired
    GetAnounceEndpoint(
            AnounceSearchService anounceSearchService) {
        this.anounceSearchService = anounceSearchService
    }

    @RequestMapping(value = "/anounces/{anounceId}", method = RequestMethod.GET)
    ResponseEntity find(@PathVariable("anounceId") String anounceId) {
        log.info("get anounce by id: {}", anounceId)
        def anounceOptional = anounceSearchService.getById(anounceId)
        if (anounceOptional.isPresent()) {
            return ResponseEntity.ok(map(anounceOptional.get()))
        } else {
            return ResponseEntity.notFound()
        }
    }

    def map(anounce) {
        new AnounceDto(
                id: anounce.id,
                title: anounce.title,
                description: anounce.description,
                creationDate: anounce.creationDate,
                location: new LocationDto(anounce.location),
                ownerId: anounce.ownerId,
                ownerName: anounce.ownerName,
                price: anounce.price
        )
    }

}
