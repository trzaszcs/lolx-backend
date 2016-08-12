package pl.poznan.lolx.domain.add

import groovy.transform.ToString

@ToString
class AnounceCreationRequest {
    String title
    String description
    String city
    String state
    String ownerId
}
