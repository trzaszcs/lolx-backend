package pl.poznan.lolx.domain.notification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


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
                        Thread.sleep(delayInMillis)
                        requestOrderChangeNotifier.notify(2)
                    } finally {
                        threadStarted = false;
                    }
                }
            }.start()
        }
    }

}
