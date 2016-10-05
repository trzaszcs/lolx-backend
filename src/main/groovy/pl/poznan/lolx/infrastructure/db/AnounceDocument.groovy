package pl.poznan.lolx.infrastructure.db

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class AnounceDocument {
    @Id
    String id
    String title
    String description
    LocationDocument location
    Date creationDate
    String ownerId
    BigDecimal price
    String imgName
    String categoryId
}
