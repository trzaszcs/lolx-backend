package pl.poznan.lolx.domain.requestOrder

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.AnounceDao
import pl.poznan.lolx.domain.SearchResult
import pl.poznan.lolx.domain.add.UserClient
import pl.poznan.lolx.domain.notification.DelayedNotification

@Component
class RequestOrderService {

    @Autowired
    AnounceDao anounceDao
    @Autowired
    RequestOrderDao requestOrderDao
    @Autowired
    UserClient userDetails
    @Autowired
    DelayedNotification delayedNotification

    String requestOrder(String anounceId, String authorId) {
        def anounce = anounceDao.find(anounceId)
        delayedNotification.run()
        String id = requestOrderDao.save(RequestOrder.buildNew(authorId, anounce.id, anounce.ownerId))
        delayedNotification.run()
        return id
    }

    void removeRequestOrder(String requestOrderId, String authorId) {
        requestOrderDao.remove(requestOrderId, authorId)
    }

    void accept(String requestOrderId, String authorId) {
        def optionalRequestOrder = requestOrderDao.findById(requestOrderId)
        optionalRequestOrder.ifPresent {
            if (anounceDao.find(it.anounceId).ownerId == authorId) {
                requestOrderDao.accept(requestOrderId)
                delayedNotification.run()
            }
        }
    }

    void reject(String requestOrderId, String authorId) {
        def optionalRequestOrder = requestOrderDao.findById(requestOrderId)
        optionalRequestOrder.ifPresent {
            if (anounceDao.find(it.anounceId).ownerId == authorId) {
                requestOrderDao.reject(requestOrderId)
                delayedNotification.run()
            }
        }
    }

    List<RequestOrder> getRequestOrdersForAnounceAuthor(String anounceId, String userId) {
        if (anounceDao.find(anounceId).ownerId == userId) {
            requestOrderDao.findByAnounceId(anounceId)
        }
        return []
    }

    Optional<DetailedRequestOrder> getRequestOrderForAnounce(String anounceId, String userId) {
        return requestOrderDao.findByAnounceIdAndAuthorIdOrAnounceAuthorId(anounceId, userId).map({
            markAsSeenAndDecorate(userId, it)
        })
    }

    Optional<DetailedRequestOrder> getRequestOrder(String id, String userId) {
        return requestOrderDao.findByIdAndAuthorId(id, userId).map({
            markAsSeenAndDecorate(userId, it)
        })
    }

    List<DetailedRequestOrder> findByAnounceAuthorId(String anounceAuthorId) {
        decorateRequestOrder(requestOrderDao.findByAnounceAuthorId(anounceAuthorId))
    }

    SearchResult<DetailedRequestOrder> find(SearchParams params) {
        int count = requestOrderDao.count(params)
        if (count > 0) {
            return new SearchResult<DetailedRequestOrder>(totalCount: count, items: decorateRequestOrder(requestOrderDao.find(params)))
        } else {
            return SearchResult.empty()
        }
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

    DetailedRequestOrder markAsSeenAndDecorate(String requesterId, RequestOrder requestOrder) {
        if (requestOrder.shouldBeMarkedAsSeen(requesterId)) {
            requestOrderDao.markAsSeen(requestOrder.id)
        }
        return decorateRequestOrder([requestOrder])[0]
    }
}
