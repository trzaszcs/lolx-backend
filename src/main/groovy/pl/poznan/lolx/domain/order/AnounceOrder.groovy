package pl.poznan.lolx.domain.order

import groovy.transform.ToString

@ToString
class AnounceOrder {
    String requestId
    String anounceId
    String preferedTime
    String preferedDate
    String customerContactInfo
}
