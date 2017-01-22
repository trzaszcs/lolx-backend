package pl.poznan.lolx.domain.requestOrder.events

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.requestOrder.RequestOrderDao

@Component
class UnseenRequestOrdersEventsService {

    @Autowired
    RequestOrderDao requestOrderDao

    List<Event> getEvents(String userId) {
        return null;
    }
}
