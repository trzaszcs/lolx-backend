package pl.poznan.lolx.domain.requestOrder

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.AnounceDao
import pl.poznan.lolx.domain.add.UserDetails

@Component
class RequestOrderService {

    @Autowired
    AnounceDao anounceDao
    @Autowired
    RequestOrderDao requestOrderDao
    @Autowired
    UserDetails userDetails

    String requestOrder(String anounceId, String authorId) {
        def anounce = anounceDao.find(anounceId)
        return requestOrderDao.save(RequestOrder.buildNew(authorId, anounce.id, anounce.ownerId))
    }

    void removeRequestOrder(String requestOrderId, String authorId) {
        requestOrderDao.remove(requestOrderId, authorId)
    }

    void accept(String requestOrderId, String authorId) {
        def optionalRequestOrder = requestOrderDao.findById(requestOrderId)
        optionalRequestOrder.ifPresent {
            if (anounceDao.find(it.anounceId).ownerId == authorId) {
                requestOrderDao.accept(requestOrderId)
            }
        }
    }

    void reject(String requestOrderId, String authorId) {
        def optionalRequestOrder = requestOrderDao.findById(requestOrderId)
        optionalRequestOrder.ifPresent {
            if (anounceDao.find(it.anounceId).ownerId == authorId) {
                requestOrderDao.reject(requestOrderId)
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
        decorateRequestOrder(requestOrderDao.findByAnounceAuthorId(anounceAuthorId))
    }

    List<DetailedRequestOrder> findForUser(String userId) {
        decorateRequestOrder(requestOrderDao.findByAnounceAuthorIdOrAuthorId(userId))
    }


    List<DetailedRequestOrder> findByAuthorId(String authorId) {
        decorateRequestOrder(requestOrderDao.findByAuthorId(authorId))
    }

    def decorateRequestOrder(requestOrderList) {
        if (requestOrderList.isEmpty()) {
            return []
        }
        def uniqueAnounceIds = requestOrderList.collect { it.anounceId }.toSet()
        def anouncesMap = uniqueAnounceIds.collectEntries {
            [(it): anounceDao.find(it)]
        }

        def userIds = requestOrderList.collect {
            [it.anounceAuthorId, it.authorId]
        }.flatten().toSet().toList()

        def usersMap = userDetails.find(userIds)

        requestOrderList.collect {
            new DetailedRequestOrder(
                    requestOrder: it,
                    anounceTitle: anouncesMap[it.anounceId].title,
                    requestOrderAuthorName: usersMap[it.authorId].name(),
                    anounceAuthorName: usersMap[it.anounceAuthorId].name()
            )
        }
    }
}
