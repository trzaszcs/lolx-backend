package pl.poznan.lolx.rest.worker

import pl.poznan.lolx.rest.add.LocationDto

import javax.validation.constraints.NotNull

class SimpleWorkerDto {
    String id
    String name
    String photoUrl
    @NotNull
    LocationDto location
    List<String> categoryIds
}
