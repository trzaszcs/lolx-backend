package pl.poznan.lolx.infrastructure

import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.Anounce
import pl.poznan.lolx.domain.AnounceDao


@Component
class AnounceInMemoryDao implements AnounceDao {

    def anounces = []

    @Override
    void save(Anounce anounce) {
        anounces.add(anounce)
    }
}
