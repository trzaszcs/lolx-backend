package pl.poznan.lolx.rest.worker

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import pl.poznan.lolx.domain.Coordinate
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
                        @RequestBody @Validated BaseWorkerDto dto) {
        if (jwtChecker.verify(authorizationHeader, dto.userId)) {
            def workerId = workerService.create(
                    dto.userId,
                    dto.description,
                    dto.categoryIds)
            return ResponseEntity.ok(new IdDto(id: workerId));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    @RequestMapping(value = "/workers/{workerId}", method = RequestMethod.PUT)
    ResponseEntity update(@PathVariable String workerId,
                          @RequestHeader(value = "Authorization") authorizationHeader,
                          @RequestBody @Validated BaseWorkerDto dto) {
        if (jwtChecker.verify(authorizationHeader, dto.userId)) {
            workerService.update(
                    workerId,
                    dto.userId,
                    dto.description,
                    dto.categoryIds)
            return ResponseEntity.ok().build()
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    @RequestMapping(value = "/workers/{workerId}", method = RequestMethod.GET)
    ResponseEntity get(@PathVariable String workerId) {
        workerService.find(workerId)
                .map({
            ResponseEntity.ok(
                    map(it)
            )
        })
                .orElseGet({ ResponseEntity.notFound().build() })
    }

    @RequestMapping(value = "/workers/{workerId}", method = RequestMethod.DELETE)
    ResponseEntity delete(@PathVariable String workerId,
                          @RequestHeader(value = "Authorization") authorizationHeader) {
        def userId = jwtChecker.subject(authorizationHeader)
        workerService.delete(workerId, userId)
        return ResponseEntity.ok().build()
    }

    @RequestMapping(value = "/workers/user/{userId}", method = RequestMethod.GET)
    ResponseEntity findForUser(@PathVariable String userId) {
        workerService.findForUser(userId)
                .map({
            ResponseEntity.ok(
                    map(it)
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
                            name: it.name,
                            photoUrl: it.photoUrl.orElse(null),
                            location: mapLocaton(it.location),
                            categoryIds: it.categories)
                }
        )
    }

    def map(worker) {
        new WorkerDto(
                id: worker.id,
                userId: worker.userId,
                description: worker.description,
                photoUrl: worker.photoUrl.orElse(null),
                categoryIds: worker.categories,
                name: worker.name,
                location: mapLocaton(worker.location)
        )
    }

    def mapLocaton(location) {
        new LocationDto(title: location.title, latitude: location.latitude, longitude: location.longitude)
    }
}
