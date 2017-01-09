package pl.poznan.lolx.infrastructure.db

import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.CrudRepository

interface RequestOrderMongoRepository extends CrudRepository<RequestOrderDocument, String> {
    List<RequestOrderDocument> findByAnounceId(String authorId)

    @Query('{$and : [{\'anounceId\': ?0}, {$or : [{\'authorId\' : ?1}, {\'anounceAuthorId\' : ?1}]}]}')
    RequestOrderDocument findByAnounceIdAndAuthorId(String anounceId, String authorId)

    @Query('{$and : [{\'id\': ?0}, {$or : [{\'authorId\' : ?1}, {\'anounceAuthorId\' : ?1}]}]}')
    RequestOrderDocument findByIdAndAuthorId(String id, String authorId)

    List<RequestOrderDocument> findByAuthorId(String authorId)

    List<RequestOrderDocument> findByAnounceAuthorId(String anounceAuthorId)

    List<RequestOrderDocument> findByAnounceAuthorIdOrAuthorId(String anounceAuthorId, String authorId)

}
