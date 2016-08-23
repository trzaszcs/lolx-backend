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
    @NotEmpty
    String city
    @NotEmpty
    String state
    @NotEmpty
    String ownerId
    @NotNull
    @Min(value = 0l)
    BigDecimal price
}
