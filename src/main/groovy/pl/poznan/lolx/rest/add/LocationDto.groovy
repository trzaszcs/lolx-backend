package pl.poznan.lolx.rest.add

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.hibernate.validator.constraints.NotEmpty

@EqualsAndHashCode
@ToString
class LocationDto {
    @NotEmpty
    String title
    double latitude
    double longitude
}
