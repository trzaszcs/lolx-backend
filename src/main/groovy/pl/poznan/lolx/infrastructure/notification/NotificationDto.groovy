package pl.poznan.lolx.infrastructure.notification

import groovy.transform.PackageScope

@PackageScope
class NotificationDto {
    String email
    String type
    RequestOrdetContextDto context
}
