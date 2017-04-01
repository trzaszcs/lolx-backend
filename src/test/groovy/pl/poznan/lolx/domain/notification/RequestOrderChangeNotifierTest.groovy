package pl.poznan.lolx.domain.notification

import pl.poznan.lolx.domain.Anounce
import pl.poznan.lolx.domain.AnounceDao
import pl.poznan.lolx.domain.add.User
import pl.poznan.lolx.domain.add.UserClient
import pl.poznan.lolx.domain.add.UserDetails
import pl.poznan.lolx.domain.requestOrder.RequestOrder
import pl.poznan.lolx.domain.requestOrder.RequestOrderDao
import pl.poznan.lolx.domain.requestOrder.Status
import spock.lang.Specification

class RequestOrderChangeNotifierTest extends Specification {

    RequestOrderChangeNotifier underTest
    RequestOrderDao requestOrderDao
    AnounceDao anounceDao
    NotificationClient notificationClient
    UserClient userDetails
    RequestOrderUrlBuilder builder

    def setup() {
        requestOrderDao = Stub(RequestOrderDao)
        anounceDao = Stub(AnounceDao)
        notificationClient = Mock(NotificationClient)
        userDetails = Stub(UserClient)
        builder = new RequestOrderUrlBuilder()
        underTest = new RequestOrderChangeNotifier(
                requestOrderDao,
                anounceDao,
                notificationClient,
                userDetails,
                builder)
    }


    def anounceTitle = "title"
    def anounceOwnerId = "ownerId"
    def userOptional = Optional.of(new User(userDetails: new UserDetails(email: "email@wp.pl")))
    def anounceOptional = new Anounce(ownerId: anounceOwnerId, title: anounceTitle)


    def "should send event created notification"() {
        given:
        def order = order(Status.WAITING)
        setupStubs(order, anounceOwnerId)
        when:
        underTest.notify(1)
        then:
        1 * notificationClient.requestCreated(userOptional.get().email().get(), builder.build(order.id), anounceTitle)
    }

    def "should send event accepted notification"() {
        given:
        def order = order(Status.ACCEPTED)
        setupStubs(order, order.authorId)
        when:
        underTest.notify(1)
        then:
        1 * notificationClient.requestAccepted(userOptional.get().email().get(), builder.build(order.id), anounceTitle)
    }

    def "should send event rejected notification"() {
        given:
        def order = order(Status.REJECTED)
        setupStubs(order, order.authorId)
        when:
        underTest.notify(1)
        then:
        1 * notificationClient.requestRejected(userOptional.get().email().get(), builder.build(order.id), anounceTitle)
    }

    def setupStubs(RequestOrder order, String userIdToGet) {
        anounceDao.find(order.anounceId) >> anounceOptional
        requestOrderDao.lockNotNotified(_) >> Optional.of(order)
        userDetails.find(userIdToGet, true) >> userOptional
    }

    def order(Status status) {
        new RequestOrder(authorId: "authorId", anounceId: "anounceId", id: "id", status: status)
    }


}
