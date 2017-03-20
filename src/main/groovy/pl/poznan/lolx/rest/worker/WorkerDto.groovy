package pl.poznan.lolx.rest.worker

import org.hibernate.validator.constraints.NotEmpty
import pl.poznan.lolx.rest.add.LocationDto

import javax.validation.constraints.NotNull


class WorkerDto {
    String id
    @NotEmpty
    String userId
    @NotEmpty
    String description
    String photoUrl
    @NotNull
    LocationDto location
    @NotEmpty
    List<String> categoryIds
}
