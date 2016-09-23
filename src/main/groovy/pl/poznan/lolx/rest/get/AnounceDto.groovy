package pl.poznan.lolx.rest.get

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import groovy.transform.ToString
import pl.poznan.lolx.rest.add.LocationDto
import pl.poznan.lolx.rest.util.DateSerializer

import javax.validation.constraints.NotNull

@ToString
class AnounceDto {
    String id
    String title
    String description
    @NotNull
    LocationDto location
    @JsonSerialize(using = DateSerializer.class)
    Date creationDate
    String ownerId
    String ownerName
}
