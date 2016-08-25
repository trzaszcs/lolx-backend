package pl.poznan.lolx.infrastructure.db

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class AnounceOrderDocument {
    @Id
    String requestId
    String requestDate
    String anounceId
    String preferedTime
    String preferedDate
    String customerContactInfo
}
