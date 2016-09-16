package pl.poznan.lolx.domain.order

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.add.UserDetails

import java.util.stream.Collectors

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
                customerContactInfo: anounceOrderRequest.customerContactInfo,
                customerId: anounceOrderRequest.customerId
        ))

        return anounceOrderRequest.requestId
    }

    AnounceOrderRequest get(String requestId) {
        AnounceOrder anounceOrder = anounceOrderDao.get(requestId)
        new AnounceOrderRequest(
                requestId: anounceOrder.requestId,
                requestDate: anounceOrder.requestDate,
                anounceId: anounceOrder.anounceId,
                preferedTime: anounceOrder.preferedTime,
                preferedDate: anounceOrder.preferedDate,
                customerContactInfo: anounceOrder.customerContactInfo,
                customerId: anounceOrder.customerId
        )
    }

    List<AnounceOrderRequest> getByCustomerId(String customerId) {
        List<AnounceOrder> orders = anounceOrderDao.getByCustomerId(customerId)
        orders.stream()
              .map( { anounceOrder ->
                    new AnounceOrderRequest(
                            requestId: anounceOrder.requestId,
                            requestDate: anounceOrder.requestDate,
                            anounceId: anounceOrder.anounceId,
                            preferedTime: anounceOrder.preferedTime,
                            preferedDate: anounceOrder.preferedDate,
                            customerContactInfo: anounceOrder.customerContactInfo,
                            customerId: anounceOrder.customerId
                    )
                })
                .collect(Collectors.toList())
    }
}
