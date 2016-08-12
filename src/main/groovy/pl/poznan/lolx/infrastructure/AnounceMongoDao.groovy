package pl.poznan.lolx.infrastructure

import org.codehaus.groovy.runtime.InvokerHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.Anounce
import pl.poznan.lolx.domain.AnounceDao
import pl.poznan.lolx.infrastructure.db.AnounceDocument
import pl.poznan.lolx.infrastructure.db.AnounceMongoRepository

@Component
class AnounceMongoDao implements AnounceDao {

    @Autowired
    AnounceMongoRepository anounceMongoRepository

    @Override
    void save(Anounce anounce) {
        def document = new AnounceDocument()
        use(InvokerHelper) {
            document.setProperties(anounce.properties)
        }
        anounceMongoRepository.save(document)
    }
}
