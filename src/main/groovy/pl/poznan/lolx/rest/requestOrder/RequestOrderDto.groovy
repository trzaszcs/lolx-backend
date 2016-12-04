package pl.poznan.lolx.rest.requestOrder

import groovy.transform.ToString
import org.hibernate.validator.constraints.NotEmpty

@ToString
class RequestOrderDto {
    @NotEmpty
    String anounceId
    @NotEmpty
    String authorId
    Date creationDate
    boolean accepted
}
