package pl.poznan.lolx.domain.worker

import pl.poznan.lolx.domain.Worker

interface WorkerDao {

    String create(Worker worker)

    void update(Worker worker)

    void delete(String id)

    Optional<Worker> find(String id)

    Optional<Worker> findForUser(String id)

}
