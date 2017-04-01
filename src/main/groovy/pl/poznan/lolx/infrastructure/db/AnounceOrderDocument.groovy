package pl.poznan.lolx.infrastructure.db

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
class AnounceOrderDocument {
    @Id
    String requestId
    String title
    String requestDate
    String anounceId
    String preferedTime
    String preferedDate
    String customerContactInfo
    @Indexed
    String customerId
    @Indexed
    String ownerId
}
