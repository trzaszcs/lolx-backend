package pl.poznan.lolx.infrastructure.requestOrder

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.requestOrder.RequestOrder
import pl.poznan.lolx.domain.requestOrder.RequestOrderDao
import pl.poznan.lolx.domain.requestOrder.SearchParams
import pl.poznan.lolx.domain.requestOrder.Status
import pl.poznan.lolx.infrastructure.db.RequestOrderDocument
import pl.poznan.lolx.infrastructure.db.RequestOrderMongoRepository

import static org.springframework.data.mongodb.core.query.Criteria.where

@Component
class RequestOrderMongoDao implements RequestOrderDao {

    @Autowired
    RequestOrderMongoRepository repository
    @Autowired
    MongoTemplate mongoTemplate

    @Override
    Optional<RequestOrder> findById(String id) {
        return Optional.ofNullable(repository.findOne(id)).map({ map(it) })
    }

    @Override
    String save(RequestOrder requestOrder) {
        return repository.save(map(requestOrder)).id
    }

    @Override
    boolean accept(String id) {
        Query query = new Query(where("id").is(id));
        def update = new Update()
        update.set("status", Status.ACCEPTED)
        update.set("seen", false)
        update.set("updateStatusDate", new Date())
        return mongoTemplate.updateFirst(query, update, RequestOrderDocument).n > 0
    }

    @Override
    boolean reject(String id) {
        Query query = new Query(where("id").is(id));
        def update = new Update()
        update.set("status", Status.REJECTED)
        update.set("seen", false)
        update.set("updateStatusDate", new Date())
        return mongoTemplate.updateFirst(query, update, RequestOrderDocument).n > 0
    }

    @Override
    boolean remove(String id, String authorId) {
        Query query = new Query(where("id").is(id).andOperator(where("authorId").is(authorId)));
        return mongoTemplate.remove(query, RequestOrderDocument).n > 0
    }

    @Override
    List<RequestOrder> findByAnounceId(String anounceId) {
        return repository.findByAnounceId(anounceId).collect { map(it) }
    }

    //TODO authorIdOrA
    @Override
    Optional<RequestOrder> findByAnounceIdAndAuthorIdOrAnounceAuthorId(String anounceId, String authorId) {
        return Optional.ofNullable(repository.findByAnounceIdAndAuthorId(anounceId, authorId))
                .map { map(it) }
    }

    @Override
    Optional<RequestOrder> findByIdAndAuthorId(String id, String authorId) {
        return Optional.ofNullable(repository.findByIdAndAuthorId(id, authorId))
                .map { map(it) }
    }

    @Override
    List<RequestOrder> findByAnounceAuthorId(String anounceAuthorId) {
        return repository.findByAnounceAuthorId(anounceAuthorId).collect { map(it) }
    }

    @Override
    List<RequestOrder> find(SearchParams params) {
        Criteria criteria = buildCriteria(params)
        Query query = new Query(criteria)
                .with(new PageRequest(params.page, params.itemsPerPage, new Sort(Sort.Direction.ASC, "creationDate")))
        return mongoTemplate.find(query, RequestOrderDocument).collect { map(it) }
    }

    @Override
    int count(SearchParams params) {
        return mongoTemplate.count(new Query(buildCriteria(params)), RequestOrderDocument)
    }

    def buildCriteria(SearchParams params) {
        Criteria criteria = new Criteria().orOperator(where("authorId").is(params.authorId), where("anounceAuthorId").is(params.authorId))
        if (params.hasStatusFilter()) {
            criteria = criteria.and("status").is(params.status)
        }
        return criteria
    }

    @Override
    List<RequestOrder> findByAuthorId(String authorId) {
        return repository.findByAuthorId(authorId).collect { map(it) }
    }

    @Override
    void markAsSeen(String id) {
        Query query = new Query(where("id").is(id));
        def update = new Update()
        update.set("seen", true)
        mongoTemplate.updateFirst(query, update, RequestOrderDocument)
    }

    @Override
    List<RequestOrder> findUnseen(String userId) {
        Criteria criteria = new Criteria().orOperator(
                where("authorId").is(userId).and("status").in(Status.ACCEPTED, Status.REJECTED),
                where("anounceAuthorId").is(userId).and("status").is(Status.WAITING))
                .and("seen").is(false)
        return mongoTemplate.find(new Query(criteria), RequestOrderDocument).collect {
            map(it)
        }
    }

    def map(RequestOrder order) {
        return new RequestOrderDocument(
                anounceId: order.anounceId,
                anounceAuthorId: order.anounceAuthorId,
                anounceType: order.anounceType,
                authorId: order.authorId,
                status: order.status,
                creationDate: order.creationDate,
                seen: order.seen
        )
    }

    def map(RequestOrderDocument order) {
        return new RequestOrder(
                id: order.id,
                anounceId: order.anounceId,
                anounceAuthorId: order.anounceAuthorId,
                anounceType: order.anounceType,
                authorId: order.authorId,
                status: order.status,
                updateStatusDate: order.updateStatusDate,
                creationDate: order.creationDate
        )
    }

}
