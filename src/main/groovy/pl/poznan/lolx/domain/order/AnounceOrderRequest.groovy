package pl.poznan.lolx.domain.order

import groovy.transform.ToString

@ToString
class AnounceOrderRequest {
    String requestId
    String anounceId
    String preferedTime
    String preferedDate
    String customerContactInfo
}
