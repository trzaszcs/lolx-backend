package pl.poznan.lolx.rest.add

import groovy.transform.ToString
import org.hibernate.validator.constraints.NotEmpty

@ToString
class AnounceRequestDto {
    @NotEmpty
    String title
    @NotEmpty
    String description
    @NotEmpty
    String city
    @NotEmpty
    String state
    @NotEmpty
    String ownerId
}
