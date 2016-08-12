package pl.poznan.lolx.infrastructure.db

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.Repository


interface AnounceMongoRepository extends CrudRepository<AnounceDocument, String> {
}
