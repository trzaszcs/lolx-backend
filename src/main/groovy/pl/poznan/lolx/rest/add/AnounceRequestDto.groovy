package pl.poznan.lolx.rest.add

import groovy.transform.ToString
import org.hibernate.validator.constraints.NotEmpty

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@ToString
class AnounceRequestDto {
    @NotEmpty
    String title
    @NotEmpty
    String description
    @NotNull
    LocationDto location
    @NotEmpty
    String ownerId
    @NotNull
    @Min(value = 0l)
    BigDecimal price
    String imgName
}
