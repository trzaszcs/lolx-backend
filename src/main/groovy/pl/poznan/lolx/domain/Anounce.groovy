package pl.poznan.lolx.domain

import groovy.transform.ToString

@ToString
class Anounce {
    String id
    String title
    String description
    String city
    String state
    Date creationDate = new Date()
    String ownerId
    String ownerName
    BigDecimal price
}
