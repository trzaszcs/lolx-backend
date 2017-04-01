package pl.poznan.lolx.rest.order

import groovy.transform.ToString

@ToString
class AnounceOrderStatusDto {
    String requestId
    String requestDate
    String anounceContactInfo
    String status
}
