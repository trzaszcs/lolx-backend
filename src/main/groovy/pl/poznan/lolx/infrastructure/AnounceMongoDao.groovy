package pl.poznan.lolx.infrastructure

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.Anounce
import pl.poznan.lolx.domain.AnounceDao
import pl.poznan.lolx.domain.add.Category
import pl.poznan.lolx.infrastructure.db.AnounceDocument
import pl.poznan.lolx.infrastructure.db.AnounceMongoRepository
import pl.poznan.lolx.infrastructure.db.LocationDocument

import javax.annotation.PostConstruct

@Component
class AnounceMongoDao implements AnounceDao {

    @Autowired
    AnounceMongoRepository anounceMongoRepository

    @Override
    void save(Anounce anounce) {
        def document = new AnounceDocument()
        document.id = anounce.id
        map(anounce, document)
        anounceMongoRepository.save(document)
    }

    void update(Anounce anounce) {
        def document = anounceMongoRepository.findOne(anounce.id)
        map(anounce, document)
        anounceMongoRepository.save(document)
    }

    @Override
    Anounce find(String id) {
        return map(anounceMongoRepository.findOne(id))
    }

    def map(anounce, document) {
        document.title = anounce.title
        document.creationDate = anounce.creationDate
        document.description = anounce.description
        document.ownerId = anounce.ownerId
        document.price = anounce.price
        document.categoryId = anounce.category.id
        document.location = new LocationDocument(
                title: anounce.location.title,
                latitude: anounce.location.latitude,
                longitude: anounce.location.longitude
        )
        document.imgName = anounce.imgName
        document.type = anounce.type
        document.duration = anounce.duration
        document.closed = anounce.closed
        document.contactPhone = anounce.contactPhone
    }

    def map(AnounceDocument anounce) {
        return new Anounce(
                id: anounce.id,
                ownerId: anounce.ownerId,
                title: anounce.title,
                description: anounce.description,
                creationDate: anounce.creationDate,
                price: anounce.price,
                duration: anounce.duration,
                category: new Category(id: anounce.categoryId),
                type: anounce.type

        )
    }
}
