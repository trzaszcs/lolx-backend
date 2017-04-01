package pl.poznan.lolx.domain.add

import groovy.transform.ToString
import pl.poznan.lolx.domain.Location

@ToString
class User {
    String id
    String firstName
    String nick
    Date created
    UserDetails userDetails

    String name() {
        nick
    }

    Optional<UserDetails> userDetails() {
        Optional.ofNullable(userDetails)
    }

    Optional<String> email() {
        userDetails().map({ it.email })
    }

    Optional<Location> location() {
        userDetails().map({ it.location })
    }

    Optional<String> photoUrl() {
        userDetails().flatMap({it.photoUrl()})
    }
}
