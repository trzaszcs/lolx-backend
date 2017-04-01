package pl.poznan.lolx.infrastructure

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.*
import pl.poznan.lolx.domain.add.Category

import javax.annotation.PostConstruct

@Component
class StartupInitializer {

    @Autowired
    SearchEngine searchEngine
    @Autowired
    AnounceDao anounceDao

    def genPrice() {
        Math.round(Math.random() * 100) / 100;
    }

    def generateSingle(id, title, ownerId) {
        new Anounce(
                id: id,
                title: title,
                description: "Lorem Ipsum ...",
                category: new Category(id: "1", name: "Sprzatanie"),
                location: new Location("Poznan, wielkopolskie", 52.406374, 16.9251681),
                ownerId: ownerId,
                ownerName: "someName",
                price: genPrice(),
                type: AnounceType.EXECUTE)
    }

    @PostConstruct
    void initAnounces() {
        def anouncesToSave = [generateSingle(0, "Wyprowadzę psa", 1), generateSingle(1, "Skoszę trawę", 1), generateSingle(2, "Udziele korepetycji", 2)]
        anouncesToSave.each {
            searchEngine.index(it)
            anounceDao.save(it)
        }
    }

}
