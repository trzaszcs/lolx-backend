package pl.poznan.lolx.rest.requestOrder

import groovy.transform.ToString
import org.hibernate.validator.constraints.NotEmpty

@ToString
class RequestRequestOrderDto {
    @NotEmpty
    String anounceId
    @NotEmpty
    String authorId
}
