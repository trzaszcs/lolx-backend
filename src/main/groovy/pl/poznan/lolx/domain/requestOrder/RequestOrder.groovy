package pl.poznan.lolx.domain.requestOrder

import groovy.transform.ToString


@ToString
class RequestOrder {
    String id
    String authorId
    String anounceId
    String anounceAuthorId
    Date creationDate
    Date updateStatusDate
    Status status

    static RequestOrder buildNew(String authorId, String anounceId, String anounceAuthorId) {
        new RequestOrder(authorId: authorId, anounceId: anounceId, creationDate: new Date(), anounceAuthorId: anounceAuthorId, status: Status.WAITING)
    }
}
