package pl.poznan.lolx.infrastructure

import org.codehaus.groovy.runtime.InvokerHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.Anounce
import pl.poznan.lolx.domain.AnounceDao
import pl.poznan.lolx.infrastructure.db.AnounceDocument
import pl.poznan.lolx.infrastructure.db.AnounceMongoRepository
import pl.poznan.lolx.infrastructure.db.LocationDocument

@Component
class AnounceMongoDao implements AnounceDao {

    @Autowired
    AnounceMongoRepository anounceMongoRepository

    @Override
    void save(Anounce anounce) {
        def document = new AnounceDocument()
        document.id = anounce.id
        document.title = anounce.title
        document.creationDate = anounce.creationDate
        document.description = anounce.description
        document.ownerId = anounce.ownerId
        document.price = anounce.price
        document.location = new LocationDocument(
                title: anounce.location.title,
                latitude: anounce.location.latitude,
                longitude: anounce.location.longitude
        )

        anounceMongoRepository.save(document)
    }
}
