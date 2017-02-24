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
        if (authorId == anounceAuthorId) {
            throw new IllegalArgumentException("Author ${authorId} cannot create requestOrder because is the owner of anounce ${anounceId}")
        }
        new RequestOrder(
                authorId: authorId,
                anounceId: anounceId,
                creationDate: new Date(),
                anounceAuthorId: anounceAuthorId,
                anounceType: anounceType,
                status: Status.WAITING,
                seen: false)
    }

    boolean shouldBeMarkedAsSeen(String requestingUserId) {
        return !seen &&
                (status == Status.WAITING && anounceAuthorId == requestingUserId) ||
                (status == Status.ACCEPTED && authorId == requestingUserId) ||
                (status == Status.REJECTED && authorId == requestingUserId)
    }

    boolean waiting() {
        this.status == Status.WAITING
    }

    boolean accepted() {
        this.status == Status.ACCEPTED
    }

    boolean rejected() {
        this.status == Status.REJECTED
    }
}
