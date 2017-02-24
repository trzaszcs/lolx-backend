package pl.poznan.lolx.domain.add

import groovy.transform.ToString

@ToString
class User {
    String id
    String email
    String firstName
    String nick
    Date created

    String name() {
        nick
    }

    Optional<String> email() {
        Optional.ofNullable(email)
    }
}
