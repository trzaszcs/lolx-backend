package pl.poznan.lolx.infrastructure.user

import groovy.transform.ToString

@ToString
class UserDto {
    String id
    String email
    String firstName
    Date created
}