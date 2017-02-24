package pl.poznan.lolx.domain.notification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.AnounceDao
import pl.poznan.lolx.domain.add.UserClient
import pl.poznan.lolx.domain.requestOrder.RequestOrderDao

@Component
class RequestOrderChangeNotifier {

    int lockTimeInMillis = 5 * 60 * 1000

    RequestOrderDao requestOrderDao
    AnounceDao anounceDao
    NotificationClient notificationClient
    UserClient userDetails
    RequestOrderUrlBuilder builder

    @Autowired
    RequestOrderChangeNotifier(
            RequestOrderDao requestOrderDao,
            AnounceDao anounceDao,
            NotificationClient notificationClient,
            UserClient userDetails,
            RequestOrderUrlBuilder builder) {
        this.requestOrderDao = requestOrderDao
        this.anounceDao = anounceDao
        this.notificationClient = notificationClient
        this.userDetails = userDetails
        this.builder = builder
    }

    void notify(int limit) {
        while (limit > 0) {
            def orderOpt = requestOrderDao.lockNotNotified(new Date(System.currentTimeMillis() + lockTimeInMillis))
            if (!orderOpt.isPresent()) {
                return
            }
            orderOpt.ifPresent({
                def anounce = anounceDao.find(it.anounceId)
                def address = builder.build(it.id)
                if (it.waiting()) {
                    notificationClient.requestCreated(emailTo(anounce.ownerId), address, anounce.title)
                } else if (it.accepted()) {
                    notificationClient.requestAccepted(emailTo(it.authorId), address, anounce.title)
                } else if (it.rejected()) {
                    notificationClient.requestRejected(emailTo(it.authorId), address, anounce.title)
                }
                requestOrderDao.markLockedAsNotified(it.id)
            })
            limit--
        }
    }

    String emailTo(String id) {
        userDetails.find(id, true).orElseThrow({
            new IllegalArgumentException("user with id $id not found")
        }).email().get()
    }
}
