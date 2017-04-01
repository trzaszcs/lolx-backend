package pl.poznan.lolx.infrastructure.user

import groovy.transform.ToString
import pl.poznan.lolx.rest.add.LocationDto

@ToString
class UserDto {
    String id
    String firstName
    String nick
    Date created

    String email
    LocationDto location
    String photoUrl
}
