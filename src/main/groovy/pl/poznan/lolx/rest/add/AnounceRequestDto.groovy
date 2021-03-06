package pl.poznan.lolx.rest.add

import groovy.transform.ToString
import org.hibernate.validator.constraints.NotEmpty
import pl.poznan.lolx.domain.AnounceDuration

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
    @NotEmpty
    String categoryId
    @NotNull
    @Min(value = 0l)
    BigDecimal price
    String imgName
    @NotNull
    AnounceDuration duration
}
