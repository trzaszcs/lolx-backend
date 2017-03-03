package pl.poznan.lolx.rest.get

import com.fasterxml.jackson.annotation.JsonFormat
import groovy.transform.ToString
import pl.poznan.lolx.rest.add.LocationDto

import javax.validation.constraints.NotNull

@ToString
class AnounceDto {
    String id
    String title
    String description
    @NotNull
    LocationDto location
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    Date creationDate
    String ownerId
    String ownerName
    BigDecimal price
    String img
}
