package pl.poznan.lolx.infrastructure.worker

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.Location
import pl.poznan.lolx.domain.Worker
import pl.poznan.lolx.domain.worker.WorkerDao
import pl.poznan.lolx.infrastructure.db.LocationDocument
import pl.poznan.lolx.infrastructure.db.WorkerDocument
import pl.poznan.lolx.infrastructure.db.WorkerMongoRepository

@Component
class WorkerMongoDao implements WorkerDao {
    @Autowired
    WorkerMongoRepository repository

    String create(Worker worker) {
        repository.save(
                WorkerDocument.create(
                        worker.userId,
                        worker.description,
                        worker.name,
                        worker.categories,
                        worker.getPhotoUrl().orElse(null),
                        map(worker.location)
                )).id
    }

    void update(Worker worker) {
        def workerDocument = Optional.ofNullable(repository.findOne(worker.id)).orElseThrow({
            new IllegalArgumentException("worker with id $worker.id not found")
        })
        workerDocument.update(
                worker.description,
                worker.getPhotoUrl().orElse(null),
                map(worker.location),
                worker.categories
        )
        repository.save(workerDocument)
    }

    void delete(String id) {
        repository.delete(id)
    }

    Optional<Worker> find(String id) {
        Optional.ofNullable(repository.findOne(id))
                .map({ map(it) })
    }

    @Override
    Optional<Worker> findForUser(String id) {
        Optional.ofNullable(repository.findByUserId(id))
                .map({ map(it) })
    }

    LocationDocument map(Location location) {
        new LocationDocument(title: location.title, latitude: location.latitude, longitude: location.longitude)
    }

    Worker map(WorkerDocument worker) {
        def location = worker.location
        new Worker(
                worker.id,
                worker.userId,
                worker.description,
                worker.photoUrl,
                worker.categoriesId,
                new Location(location.title, location.latitude, location.longitude),
                worker.name
        )
    }

}
