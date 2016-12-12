package pl.poznan.lolx.rest.requestOrder

import groovy.transform.ToString
import org.hibernate.validator.constraints.NotEmpty

@ToString
class DetailedRequestOrderDto extends RequestOrderDto {
    @NotEmpty
    String anounceTitle
}
