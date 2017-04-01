package pl.poznan.lolx.domain.order

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.AnounceSearchService
import pl.poznan.lolx.domain.add.UserClient

import java.util.stream.Collectors

@Component
class AnounceOrderService {

    @Autowired
    AnounceOrderDao anounceOrderDao

    @Autowired
    AnounceSearchService anounceSearchService

    @Autowired
    UserClient userDetails


    String requestOrder(String anounceId, String authorId) {
        return null
    }

    void removeRequestOrder(String requestOrderID) {

    }

    void acceptOrder(String requestOrderId, String authorId) {
    }

    void getRequestOrders(String anounceId, String userId){

    }

    void getRequestOrderForAnounce(String anounceId, String userId){

    }

    String order(AnounceOrderRequest anounceOrderRequest) {

        def anounce = anounceSearchService.getById(anounceOrderRequest.anounceId)

        anounceOrderDao.order(new AnounceOrder(
                requestId: anounceOrderRequest.requestId,
                title: anounce.get().title,
                requestDate: System.currentTimeMillis(),
                anounceId: anounceOrderRequest.anounceId,
                preferedTime: anounceOrderRequest.preferedTime,
                preferedDate: anounceOrderRequest.preferedDate,
                customerContactInfo: anounceOrderRequest.customerContactInfo,
                customerId: anounceOrderRequest.customerId,
                ownerId: anounce.get().ownerId
        ))

        return anounceOrderRequest.requestId
    }

    AnounceOrderRequest get(String requestId) {
        AnounceOrder anounceOrder = anounceOrderDao.get(requestId)
        new AnounceOrderRequest(
                requestId: anounceOrder.requestId,
                title: anounceOrder.title,
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
                .map({ anounceOrder ->
            new AnounceOrderRequest(
                    requestId: anounceOrder.requestId,
                    title: anounceOrder.title,
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

    List<AnounceOrderRequest> getByOwnerId(String ownerId) {
        List<AnounceOrder> orders = anounceOrderDao.getByOwnerId(ownerId)
        orders.stream()
                .map({ anounceOrder ->
            new AnounceOrderRequest(
                    requestId: anounceOrder.requestId,
                    title: anounceOrder.title,
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
