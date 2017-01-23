package pl.poznan.lolx.domain.requestOrder.events

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.requestOrder.RequestOrderDao
import pl.poznan.lolx.domain.requestOrder.Status

@Component
class UnseenRequestOrdersEventsService {

    @Autowired
    RequestOrderDao requestOrderDao

    List<Event> getEvents(String userId) {
        assert userId
        requestOrderDao.findUnseen(userId).collect {
            if (it.authorId == userId && it.status == Status.ACCEPTED) {
                return Event.accepted(it.id)
            } else if (it.authorId == userId && it.status == Status.REJECTED) {
                return Event.rejected(it.id)
            } else if (it.anounceAuthorId == userId) {
                return Event.created(it.id)
            } else {
                throw new UnsupportedOperationException("Don't know how to build event for requestOrder" + it)
            }
        }
    }
}
