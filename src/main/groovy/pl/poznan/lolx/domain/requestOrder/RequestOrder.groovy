package pl.poznan.lolx.domain.requestOrder

import groovy.transform.ToString


@ToString
class RequestOrder {
    String id
    String authorId
    String anounceId
    Date creationDate
    boolean accepted

    static RequestOrder buildNew(String authorId, String anounceId) {
        new RequestOrder(authorId: authorId, anounceId: anounceId, creationDate: new Date())
    }
}
