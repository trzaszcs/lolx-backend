package pl.poznan.lolx.rest.get

import groovy.transform.ToString

@ToString
class AnounceDto {
    String id
    String title
    String description
    String city
    String state
    Date creationDate
    String ownerId
    String ownerName
}
