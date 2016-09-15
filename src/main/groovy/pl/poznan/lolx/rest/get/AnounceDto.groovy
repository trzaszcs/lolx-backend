package pl.poznan.lolx.rest.get

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import groovy.transform.ToString
import pl.poznan.lolx.rest.util.DateSerializer

@ToString
class AnounceDto {
    String id
    String title
    String description
    String city
    String state
    @JsonSerialize(using = DateSerializer.class)
    Date creationDate
    String ownerId
    String ownerName
    BigDecimal price
}
