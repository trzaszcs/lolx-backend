package pl.poznan.lolx.rest.add

import groovy.transform.ToString
import org.hibernate.validator.constraints.NotEmpty

@ToString
class LocationDto {
    @NotEmpty
    String title
    double latitude
    double longitude
}
