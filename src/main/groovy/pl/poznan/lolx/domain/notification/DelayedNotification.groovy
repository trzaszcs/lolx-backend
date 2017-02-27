package pl.poznan.lolx.domain.notification

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Slf4j
@Component
class DelayedNotification {

    int delayInMillis = 30 * 1000 // 30 sec
    synchronized threadStarted = false

    RequestOrderChangeNotifier requestOrderChangeNotifier

    @Autowired
    DelayedNotification(RequestOrderChangeNotifier requestOrderChangeNotifier) {
        this.requestOrderChangeNotifier = requestOrderChangeNotifier
    }

    synchronized void run() {
        if (!threadStarted) {
            threadStarted = true
            new Thread() {
                @Override
                void run() {
                    try {
                        log.info("delaying notifiation to ${delayInMillis / 1000} sec")
                        Thread.sleep(delayInMillis)
                        requestOrderChangeNotifier.notify(2)
                    } catch (Exception ex) {
                        log.warn("uncaught exception", ex)
                    } finally {
                        threadStarted = false;
                    }
                }
            }.start()
        }
    }

}
