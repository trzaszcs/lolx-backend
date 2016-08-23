package pl.poznan.lolx.rest.find

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import groovy.transform.ToString
import pl.poznan.lolx.rest.util.DateSerializer

@ToString
class SimpleAnounceDto {
    String id
    String title
    String city
    String state
    BigDecimal price
    @JsonSerialize(using = DateSerializer.class)
    Date creationDate
}
