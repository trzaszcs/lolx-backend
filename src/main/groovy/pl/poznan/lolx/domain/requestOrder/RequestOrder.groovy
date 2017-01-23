package pl.poznan.lolx.domain.requestOrder

import groovy.transform.ToString
import pl.poznan.lolx.domain.AnounceType


@ToString
class RequestOrder {
    String id
    String authorId
    String anounceId
    String anounceAuthorId
    AnounceType anounceType
    Date creationDate
    Date updateStatusDate
    Status status
    boolean seen

    static RequestOrder buildNew(String authorId, String anounceId, String anounceAuthorId, AnounceType anounceType) {
        new RequestOrder(
                authorId: authorId,
                anounceId: anounceId,
                creationDate: new Date(),
                anounceAuthorId: anounceAuthorId,
                anounceType: anounceType,
                status: Status.WAITING,
                seen: false)
    }
}
