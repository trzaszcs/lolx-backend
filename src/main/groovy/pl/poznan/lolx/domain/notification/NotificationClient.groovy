package pl.poznan.lolx.domain.notification


interface NotificationClient {
    void requestCreated(String to, String requestOrderUrl, String anounceTitle)

    void requestAccepted(String to, String requestOrderUrl, String anounceTitle)

    void requestRejected(String to, String requestOrderUrl, String anounceTitle)
}
