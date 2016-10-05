package pl.poznan.lolx.domain.add

import groovy.transform.ToString
import pl.poznan.lolx.domain.Location

@ToString
class AnounceCreationRequest {
    String title
    String description
    Location location
    String ownerId
    BigDecimal price
    String imgName
    String categoryId
}
