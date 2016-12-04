package pl.poznan.lolx.infrastructure.db

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import pl.poznan.lolx.domain.AnounceDuration
import pl.poznan.lolx.domain.AnounceType

@Document
class RequestOrderDocument {
    @Id
    String id
    String authorId
    String anounceId
    Date creationDate
    boolean accepted
}
