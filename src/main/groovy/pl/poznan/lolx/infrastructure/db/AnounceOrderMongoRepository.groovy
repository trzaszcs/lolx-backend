package pl.poznan.lolx.infrastructure.db

import org.springframework.data.repository.CrudRepository

interface AnounceOrderMongoRepository extends CrudRepository<AnounceOrderDocument, String> {
    List<AnounceOrderDocument> getByCustomerId(String customerId)
    List<AnounceOrderDocument> getByOwnerId(String ownerId)
}
