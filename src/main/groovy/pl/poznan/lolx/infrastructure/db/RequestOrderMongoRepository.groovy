package pl.poznan.lolx.infrastructure.db

import org.springframework.data.repository.CrudRepository

interface RequestOrderMongoRepository extends CrudRepository<RequestOrderDocument, String> {
    List<RequestOrderDocument> findByAnounceId(String authorId)
    RequestOrderDocument findByAnounceIdAndAuthorId(String anounceId, String authorId)
    RequestOrderDocument findByIdAndAuthorId(String id, String authorId)
}
