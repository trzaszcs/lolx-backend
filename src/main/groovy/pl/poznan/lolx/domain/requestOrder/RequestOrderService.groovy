package pl.poznan.lolx.domain.requestOrder

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.AnounceDao

@Component
class RequestOrderService {

    @Autowired
    AnounceDao anounceDao
    @Autowired
    RequestOrderDao requestOrderDao

    String requestOrder(String anounceId, String authorId) {
        def anounce = anounceDao.find(anounceId)
        return requestOrderDao.save(RequestOrder.buildNew(authorId, anounce.id, anounce.ownerId))
    }

    void removeRequestOrder(String requestOrderId, String authorId) {
        requestOrderDao.remove(requestOrderId, authorId)
    }

    void acceptOrder(String requestOrderId, String authorId) {
        def optionalRequestOrder = requestOrderDao.findById(requestOrderId)
        optionalRequestOrder.ifPresent {
            if (anounceDao.find(it.anounceId).ownerId == authorId) {
                requestOrderDao.accept(requestOrderId)
            }
        }
    }

    List<RequestOrder> getRequestOrdersForAnounceAuthor(String anounceId, String userId) {
        if (anounceDao.find(anounceId).ownerId == userId) {
            requestOrderDao.findByAnounceId(anounceId)
        }
        return []
    }

    Optional<RequestOrder> getRequestOrderForAnounce(String anounceId, String userId) {
        return requestOrderDao.findByAnounceIdAndAuthorId(anounceId, userId)
    }

    Optional<DetailedRequestOrder> getRequestOrder(String id, String userId) {
        return requestOrderDao.findByIdAndAuthorId(id, userId)
    }

    List<DetailedRequestOrder> findByAnounceAuthorId(String anounceAuthorId) {
        enrichRequestOrdersWithAnounce(requestOrderDao.findByAnounceIdAndAuthorId(anounceAuthorId))
    }


    List<DetailedRequestOrder> findByAuthorId(String authorId) {
        enrichRequestOrdersWithAnounce(requestOrderDao.findByAuthorId(authorId))
    }

    def enrichRequestOrdersWithAnounce(list) {
        list.collect {
            new DetailedRequestOrder(requestOrder: it, anounceTitle: anounceDao.find(it.anounceId).title)
        }
    }
}
