package pl.poznan.lolx.rest.order

import groovy.transform.ToString
import org.hibernate.validator.constraints.NotEmpty

@ToString
class AnounceOrderRequestDto {
    @NotEmpty
    String requestId
    @NotEmpty
    String anounceId
    String preferedTime
    String preferedDate
    String customerContactInfo
    String ownerId
}
