package pl.poznan.lolx.rest.find

import com.fasterxml.jackson.annotation.JsonFormat
import groovy.transform.ToString
import pl.poznan.lolx.rest.add.LocationDto

import javax.validation.constraints.NotNull

@ToString
class SimpleAnounceDto {
    String id
    String title
    @NotNull
    LocationDto location
    BigDecimal price
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    Date creationDate
    String img
    String type
}
