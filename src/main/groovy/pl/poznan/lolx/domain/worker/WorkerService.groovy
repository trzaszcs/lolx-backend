package pl.poznan.lolx.domain.worker

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.*
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

    String create(String userId, String description, String photoUrl, List<String> categoryIds, Location location) {
        checkCategories(categoryIds)
        def userDetails = userClient.find(userId, false)
                .orElseThrow({ new IllegalArgumentException("user with id $userId not found") })
        def worker = new Worker(
                null,
                userId,
                description,
                photoUrl,
                categoryIds,
                location,
                userDetails.name()
        )
        def id = dao.create(worker)
        def savedWorker = worker.withId(id)
        searchEngine.index(savedWorker)
        return id
    }

    Worker update(String id, String userId, String description, String photoUrl, List<String> categoryIds, Location location) {
        assert id != null && !id.isEmpty()
        checkCategories(categoryIds)
        def userDetails = userClient.find(userId, false)
                .orElseThrow({ new IllegalArgumentException("user with id $userId not found") })
        def worker = new Worker(id, userId, description, photoUrl, categoryIds, location, userDetails.name())
        searchEngine.index(worker)
        dao.update(worker)
    }

    void delete(String id) {
        assert id != null && !id.isEmpty()
        dao.delete(id)
        searchEngine.deleteWorker(id)
    }

    Optional<Worker> find(String id) {
        dao.find(id)
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
