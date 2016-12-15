package pl.poznan.lolx.infrastructure.requestOrder

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.requestOrder.RequestOrder
import pl.poznan.lolx.domain.requestOrder.RequestOrderDao
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
        return mongoTemplate.updateFirst(query, update, RequestOrderDocument).n > 0
    }

    @Override
    boolean reject(String id) {
        Query query = new Query(where("id").is(id));
        def update = new Update()
        update.set("status", Status.REJECTED)
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

    @Override
    Optional<RequestOrder> findByAnounceIdAndAuthorId(String anounceId, String authorId) {
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
    List<RequestOrder> findByAnounceAuthorIdOrAuthorId(String authorId) {
        return repository.findByAnounceAuthorIdOrAuthorId(authorId, authorId).collect { map(it) }
    }

    @Override
    List<RequestOrder> findByAuthorId(String authorId) {
        return repository.findByAuthorId(authorId).collect { map(it) }
    }

    def map(RequestOrder order) {
        return new RequestOrderDocument(
                anounceId: order.anounceId,
                anounceAuthorId: order.anounceAuthorId,
                authorId: order.authorId,
                status: order.status,
                creationDate: order.creationDate
        )
    }

    def map(RequestOrderDocument order) {
        return new RequestOrder(
                id: order.id,
                anounceId: order.anounceId,
                anounceAuthorId: order.anounceAuthorId,
                authorId: order.authorId,
                status: order.status,
                creationDate: order.creationDate
        )
    }

}
