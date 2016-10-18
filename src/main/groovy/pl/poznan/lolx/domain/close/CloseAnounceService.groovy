package pl.poznan.lolx.domain.close

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.Anounce
import pl.poznan.lolx.domain.AnounceDao
import pl.poznan.lolx.domain.AnounceSearchService

@Component
class CloseAnounceService {

    @Autowired
    AnounceSearchService anounceSearchService
    @Autowired
    AnounceDao anounceDao

    void close(Anounce anounce) {
        anounce.markAsClosed()
        anounceDao.update(anounce)
        anounceSearchService.delete(anounce.id)
    }

}
