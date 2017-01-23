package pl.poznan.lolx.rest.requestOrder.events

import groovy.transform.PackageScope
import pl.poznan.lolx.domain.requestOrder.events.EventType


@PackageScope
class EventDto {
    String id;
    EventType type;
}
