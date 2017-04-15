package pl.poznan.lolx.infrastructure

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.Anounce
import pl.poznan.lolx.domain.AnounceDao
import pl.poznan.lolx.domain.Location
import pl.poznan.lolx.domain.SearchEngine
import pl.poznan.lolx.domain.add.Category
import pl.poznan.lolx.domain.worker.WorkerService

import javax.annotation.PostConstruct

@Profile("!test")
@Component
class StartupInitializer {

    @Autowired
    SearchEngine searchEngine
    @Autowired
    AnounceDao anounceDao

    @Autowired
    WorkerService workerService

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
                price: genPrice())
    }

    @PostConstruct
    void init() {
        initAnounces()
        initWorkers()
    }


    void initAnounces() {
        def anouncesToSave = [generateSingle(0, "Wyprowadzę psa", 1), generateSingle(1, "Skoszę trawę", 1), generateSingle(2, "Udziele korepetycji", 2)]
        anouncesToSave.each {
            searchEngine.index(it)
            anounceDao.save(it)
        }
    }

    void initWorkers() {
        workerService.create("3", "Jestem super pracownikiem", ["1","2"])
    }

}
