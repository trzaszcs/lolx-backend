package pl.poznan.lolx.domain.order

import groovy.transform.ToString

@ToString
class AnounceOrderRequest {
    String requestId
    String title
    String requestDate
    String anounceId
    String preferedTime
    String preferedDate
    String customerContactInfo
    String customerId
}
