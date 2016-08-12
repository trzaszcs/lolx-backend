package pl.poznan.lolx.rest.add

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import pl.poznan.lolx.domain.add.AnounceCreationRequest
import pl.poznan.lolx.domain.add.CreateAnounceService

@RestController
@Slf4j
class AddAnounceEndpoint {

    CreateAnounceService createAnounceService

    @Autowired
    AddAnounceEndpoint(CreateAnounceService createAnounceService) {
        this.createAnounceService = createAnounceService
    }

    @RequestMapping(value = "/anounces", method = RequestMethod.POST)
    AnounceIdDto add(@RequestBody @Validated AnounceRequestDto dto) {
        log.info("got new anounce {}", dto)
        new AnounceIdDto(createAnounceService.create(map(dto)))
    }

    def map(AnounceRequestDto anounceRequestDto) {
        new AnounceCreationRequest(
                title: anounceRequestDto.title,
                description: anounceRequestDto.description,
                state: anounceRequestDto.state,
                city: anounceRequestDto.state,
                ownerId: anounceRequestDto.ownerId
        )
    }

}
