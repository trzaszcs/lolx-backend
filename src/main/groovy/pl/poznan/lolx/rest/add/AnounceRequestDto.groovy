package pl.poznan.lolx.rest.add

import groovy.transform.ToString

@ToString
class AnounceRequestDto {
    String title
    String description
    String city
    String state
}
