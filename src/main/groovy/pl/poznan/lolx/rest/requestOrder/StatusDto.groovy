package pl.poznan.lolx.rest.requestOrder

import groovy.transform.PackageScope

@PackageScope
enum StatusDto {
    ALL, PENDING, ACCEPTED, REJECTED

    boolean all() {
        this == ALL
    }
}