package pl.poznan.lolx.domain.add

import groovy.transform.ToString
import pl.poznan.lolx.domain.Location

@ToString
class UserDetails {
    String email
    Location location
    String photoUrl

    Optional<String> photoUrl() {
        Optional.ofNullable(photoUrl)
    }
}
