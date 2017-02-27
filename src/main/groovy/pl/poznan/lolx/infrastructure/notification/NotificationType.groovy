package pl.poznan.lolx.infrastructure.notification

import groovy.transform.PackageScope

@PackageScope
enum NotificationType {
    ORDER_CREATED("order-created"), ORDER_ACCEPTED("order-accepted"), ORDER_REJECTED("order-rejected");

    private final String type

    NotificationType(String type) {
        this.type = type
    }

    public String getType() {
        return this.type;
    }
}
