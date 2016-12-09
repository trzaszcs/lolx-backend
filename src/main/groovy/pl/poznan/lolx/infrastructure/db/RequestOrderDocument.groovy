package pl.poznan.lolx.infrastructure.db

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import pl.poznan.lolx.domain.requestOrder.Status

@Document
class RequestOrderDocument {
    @Id
    String id
    String authorId
    String anounceId
    String anounceAuthorId
    Date creationDate
    Status status
}
