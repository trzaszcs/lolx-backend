package pl.poznan.lolx.domain.add

import groovy.transform.ToString

@ToString
class User {
    String id
    String email
    String firstName
    Date created

    String name() {
        firstName
    }
}
