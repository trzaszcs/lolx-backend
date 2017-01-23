package pl.poznan.lolx.domain.requestOrder.events


class Event {
    String requestOrderId
    EventType type

    static Event accepted(String id) {
        return new Event(requestOrderId: id, type: EventType.REQUEST_ORDER_ACCEPTED)
    }

    static Event rejected(String id) {
        return new Event(requestOrderId: id, type: EventType.REQUEST_ORDER_REJECTED)
    }

    static Event created(String id) {
        return new Event(requestOrderId: id, type: EventType.NEW_REQUEST_ORDER)
    }
}
