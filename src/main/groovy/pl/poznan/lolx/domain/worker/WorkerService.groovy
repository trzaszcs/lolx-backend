package pl.poznan.lolx.domain.worker

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.PermissionDeniedDataAccessException
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.Coordinate
import pl.poznan.lolx.domain.SearchEngine
import pl.poznan.lolx.domain.SearchResult
import pl.poznan.lolx.domain.Worker
import pl.poznan.lolx.domain.add.CategoryDetails
import pl.poznan.lolx.domain.add.UserClient

@Component
class WorkerService {

    @Autowired
    WorkerDao dao
    @Autowired
    CategoryDetails categoryDetails
    @Autowired
    SearchEngine searchEngine
    @Autowired
    UserClient userClient

    String create(String userId, String description, List<String> categoryIds) {
        checkCategories(categoryIds)
        def userDetails = userClient.find(userId, true)
                .orElseThrow({ new IllegalArgumentException("user with id $userId not found") })
        def worker = new Worker(
                null,
                userId,
                description,
                userDetails.photoUrl().orElse(null),
                categoryIds,
                userDetails.location().get(),
                userDetails.name()
        )
        def id = dao.create(worker)
        def savedWorker = worker.withId(id)
        searchEngine.index(savedWorker)
        return id
    }

    boolean update(String id, String userId, String description, List<String> categoryIds) {
        assert id != null && !id.isEmpty()
        checkCategories(categoryIds)
        dao.find(id)
                .map({ workerToUpdate ->
            if (workerToUpdate.userId != userId) {
                throw new PermissionDeniedDataAccessException("userId ${userId} not allowed to change worker ${id}")
            }
            def updatedWorker = workerToUpdate.update(description, categoryIds)
            dao.update(updatedWorker)
            searchEngine.index(updatedWorker)
            return true
        })
                .orElse(false)
    }

    void delete(String workerId, String userId) {
        assert workerId != null && !workerId.isEmpty()
        assert userId != null && !userId.isEmpty()

        dao.find(workerId).ifPresent({ worker ->
            if (worker.userId != userId) {
                throw new PermissionDeniedDataAccessException("userId ${userId} not allowed to change worker ${id}")
            }
            dao.delete(workerId)
            searchEngine.deleteWorker(workerId)
        })
    }

    Optional<Worker> find(String id) {
        dao.find(id)
    }

    Optional<Worker> findForUser(String id) {
        dao.findForUser(id)
    }

    SearchResult<Worker> find(String categoryId, Coordinate coordinate, int page, int itemsPerPage) {
        searchEngine.findWorkers(categoryId, coordinate, page, itemsPerPage)
    }

    private List<Category> checkCategories(List<String> categoryIds) {
        categoryIds.forEach {
            categoryDetails.find(it).orElseThrow({ new IllegalArgumentException("Category $it not found") })
        }
    }
}
