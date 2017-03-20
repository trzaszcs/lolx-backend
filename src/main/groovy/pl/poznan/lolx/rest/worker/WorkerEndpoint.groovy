package pl.poznan.lolx.rest.worker

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import pl.poznan.lolx.domain.Coordinate
import pl.poznan.lolx.domain.Location
import pl.poznan.lolx.domain.worker.WorkerService
import pl.poznan.lolx.rest.add.LocationDto
import pl.poznan.lolx.rest.shared.jwt.JwtChecker

@RestController
@Slf4j
class WorkerEndpoint {

    @Autowired
    WorkerService workerService
    @Autowired
    JwtChecker jwtChecker

    @RequestMapping(value = "/workers", method = RequestMethod.POST)
    ResponseEntity find(@RequestHeader(value = "Authorization") authorizationHeader,
                        @RequestBody @Validated WorkerDto dto) {
        if (jwtChecker.verify(authorizationHeader, dto.userId)) {
            def workerId = workerService.create(
                    dto.userId,
                    dto.description,
                    dto.photoUrl,
                    dto.categoryIds,
                    new Location(dto.location.title, dto.location.latitude, dto.location.longitude))
            return ResponseEntity.ok(new IdDto(id: workerId));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    @RequestMapping(value = "/workers/{workerId}", method = RequestMethod.PUT)
    ResponseEntity update(@PathVariable String workerId,
                          @RequestHeader(value = "Authorization") authorizationHeader,
                          @RequestBody @Validated WorkerDto dto) {
        if (workerId == dto.id && jwtChecker.verify(authorizationHeader, dto.userId)) {
            workerService.update(
                    dto.id,
                    dto.userId,
                    dto.description,
                    dto.photoUrl,
                    dto.categoryIds,
                    new Location(dto.location.title, dto.location.latitude, dto.location.longitude))
            return ResponseEntity.ok().build()
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    @RequestMapping(value = "/workers/{workerId}", method = RequestMethod.GET)
    ResponseEntity get(@PathVariable String workerId) {
        workerService.find(workerId)
                .map({
            ResponseEntity.ok(
                    new WorkerDto(
                            id: it.id,
                            userId: it.userId,
                            description: it.description,
                            photoUrl: it.photoUrl.orElse(null),
                            categoryIds: it.categories,
                            location: new LocationDto(title: it.locationTitle, latitude: it.latitude, longitude: it.longitude)
                    )
            )
        })
                .orElseGet({ ResponseEntity.notFound().build() })
    }

    @RequestMapping(value = "/workers", method = RequestMethod.GET)
    SearchResultDto find(@RequestParam(name = "page", defaultValue = "0") int page,
                         @RequestParam(name = "itemsPerPage", defaultValue = "20") int itemsPerPage,
                         @RequestParam(name = "latitude", required = false) Integer latitude,
                         @RequestParam(name = "longitude", required = false) Integer longitude,
                         @RequestParam(name = "categoryId", required = false) String categoryId) {

        def searchResult = workerService.find(
                categoryId,
                latitude != null && latitude != null ? new Coordinate(latitude, longitude) : null,
                page,
                itemsPerPage
        )

        new SearchResultDto(
                totalCount: searchResult.totalCount,
                workers: searchResult.items.collect {
                    new SimpleWorkerDto(
                            id: it.id,
                            name: it.name.orElseThrow { new IllegalArgumentException("name should be set")},
                            photoUrl: it.photoUrl.orElse(null),
                            location: new LocationDto(title: it.location.title, latitude: it.location.latitude, longitude: it.location.longitude))
                }
        )
    }
}
