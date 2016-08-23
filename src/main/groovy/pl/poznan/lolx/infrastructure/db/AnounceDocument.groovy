package pl.poznan.lolx.infrastructure.db

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class AnounceDocument {
    @Id
    String id
    String title
    String description
    String city
    String state
    Date creationDate
    String ownerId
    BigDecimal price
}
