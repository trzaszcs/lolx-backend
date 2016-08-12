package pl.poznan.lolx.domain.add

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.Anounce
import pl.poznan.lolx.domain.AnounceDao
import pl.poznan.lolx.domain.SearchEngine

@Component
class CreateAnounceService {

    @Autowired
    AnounceDao anounceDao
    @Autowired
    SearchEngine searchEngine

    String create(AnounceCreationRequest anounceRequest) {
        def anounceId = genId()


        def anounce = new Anounce(
                id: anounceId,
                title: anounceRequest.title,
                description: anounceRequest.description,
                state: anounceRequest.state,
                city: anounceRequest.city,
                ownerId: anounceRequest.ownerId
        )

        searchEngine.index(anounce)
        anounceDao.save(anounce)
        return anounceId
    }

    def genId() {
        UUID.randomUUID().toString()
    }
}
