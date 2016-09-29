package pl.poznan.lolx.rest.find

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import groovy.transform.ToString
import pl.poznan.lolx.rest.add.LocationDto
import pl.poznan.lolx.rest.util.DateSerializer

import javax.validation.constraints.NotNull

@ToString
class SimpleAnounceDto {
    String id
    String title
    @NotNull
    LocationDto location
    BigDecimal price
    @JsonSerialize(using = DateSerializer.class)
    Date creationDate
    String img
}
