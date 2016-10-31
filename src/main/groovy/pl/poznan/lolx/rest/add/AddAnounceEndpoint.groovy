package pl.poznan.lolx.rest.add

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import pl.poznan.lolx.domain.Location
import pl.poznan.lolx.domain.add.AnounceCreationRequest
import pl.poznan.lolx.domain.add.CreateAnounceService
import pl.poznan.lolx.domain.jwt.JwtChecker

@RestController
@Slf4j
class AddAnounceEndpoint {

    CreateAnounceService createAnounceService
    JwtChecker jwtChecker

    @Autowired
    AddAnounceEndpoint(
            CreateAnounceService createAnounceService,
            JwtChecker jwtChecker) {
        this.createAnounceService = createAnounceService
        this.jwtChecker = jwtChecker
    }

    @RequestMapping(value = "/anounces", method = RequestMethod.POST)
    ResponseEntity add(
            @RequestHeader(value = "Authorization") authorizationHeader,
            @RequestBody @Validated AnounceRequestDto dto) {
        log.info("got new anounce {}", dto)

        if (!jwtChecker.verify(authorizationHeader, dto.ownerId)) {
            log.warn("rejecting anounce {} due to authorization error", dto)
            return new ResponseEntity(
                    HttpStatus.UNAUTHORIZED)
        }

        new ResponseEntity(
                new AnounceIdDto(id: createAnounceService.create(map(dto))),
                HttpStatus.CREATED)
    }

    def map(AnounceRequestDto anounceRequestDto) {
        new AnounceCreationRequest(
                title: anounceRequestDto.title,
                description: anounceRequestDto.description,
                location: new Location(anounceRequestDto.location.title, anounceRequestDto.location.latitude, anounceRequestDto.location.longitude),
                ownerId: anounceRequestDto.ownerId,
                price: anounceRequestDto.price,
                imgName: anounceRequestDto.imgName,
                categoryId: anounceRequestDto.categoryId,
                type: anounceRequestDto.type,
                duration: anounceRequestDto.duration,
                contactPhone: anounceRequestDto.contactPhone
        )
    }

}
