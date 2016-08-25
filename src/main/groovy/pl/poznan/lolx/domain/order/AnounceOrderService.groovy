package pl.poznan.lolx.domain.order

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.add.UserDetails

@Component
class AnounceOrderService {

    @Autowired
    AnounceOrderDao anounceOrderDao
    @Autowired
    UserDetails userDetails

    String order(AnounceOrderRequest anounceOrderRequest) {
        anounceOrderDao.order(new AnounceOrder(
                requestId: anounceOrderRequest.requestId,
                requestDate: System.currentTimeMillis(),
                anounceId: anounceOrderRequest.anounceId,
                preferedTime: anounceOrderRequest.preferedTime,
                preferedDate: anounceOrderRequest.preferedDate,
                customerContactInfo: anounceOrderRequest.customerContactInfo
        ))

        return anounceOrderRequest.requestId
    }

}
