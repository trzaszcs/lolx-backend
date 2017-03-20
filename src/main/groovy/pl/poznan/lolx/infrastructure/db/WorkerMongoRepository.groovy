package pl.poznan.lolx.infrastructure.db

import org.springframework.data.repository.CrudRepository

interface WorkerMongoRepository extends CrudRepository<WorkerDocument, String> {
    List<WorkerDocument> findByCategoriesIdIn(String categoryId)
}
