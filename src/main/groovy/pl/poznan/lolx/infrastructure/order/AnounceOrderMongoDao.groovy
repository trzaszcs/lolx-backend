package pl.poznan.lolx.infrastructure.order

import org.codehaus.groovy.runtime.InvokerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.poznan.lolx.domain.order.AnounceOrder;
import pl.poznan.lolx.domain.order.AnounceOrderDao
import pl.poznan.lolx.infrastructure.db.AnounceOrderDocument
import pl.poznan.lolx.infrastructure.db.AnounceOrderMongoRepository;

@Component
class AnounceOrderMongoDao implements AnounceOrderDao {

    @Autowired
    AnounceOrderMongoRepository anounceOrderMongoRepository

    @Override
    void order(AnounceOrder anounceOrder) {
        def document = new AnounceOrderDocument()
        use(InvokerHelper) {
            document.setProperties(anounceOrder.properties)
        }
        anounceOrderMongoRepository.save(document)
    }

    @Override
    AnounceOrder get(String id) {
        def anounceOrder = new AnounceOrder()
        def document = anounceOrderMongoRepository.findOne(id)
        use(InvokerHelper) {
            anounceOrder.setProperties(document.properties)
        }
        return anounceOrder
    }
}