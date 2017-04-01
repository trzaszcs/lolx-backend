package pl.poznan.lolx.rest.worker

import org.junit.Test
import pl.poznan.lolx.domain.Location
import pl.poznan.lolx.domain.Worker
import pl.poznan.lolx.rest.IntTest
import pl.poznan.lolx.rest.add.LocationDto

class WorkerIntTest extends IntTest {

    def userNick = "someNick"
    def location = new Location("Poz",10.1, 20.33)

    @Test
    void "should add worker"() {
        // given
        def worker = sampleWorker()
        mockUsers(userNick, location)
        // when
        def response = httpCreateWorker(worker)
        // then
        assert response.status == 200
        def workerId = response.data.id
        assert workerId
        def workerFromServer = httpGetWorker(workerId)
        // compare response to request
        def responseDetails = workerFromServer.data
        assertWorkers(responseDetails, worker.userId, worker.categoryIds, location)
    }

    @Test
    void "should find added worker by categoryId"() {
        // given
        def worker = sampleWorker()
        mockUsers(userNick, location)
        def response = httpCreateWorker(worker)
        response.status == 200
        def workerId = response.data.id
        // when
        response = httpFindWorker(worker.categoryIds[0], null, null)
        // then
        assert response.status == 200
        def searchResult = response.data
        assert searchResult.totalCount == 1
        assert searchResult.workers.size() == 1
        def foundWorker = searchResult.workers[0]
        assert foundWorker.id == workerId
        assert foundWorker.name == userNick
        assertLocation(foundWorker, location)
    }

    @Test
    void "should find added worker for user"() {
        // given
        def worker = sampleWorker()
        mockUsers(userNick, location)
        def response = httpCreateWorker(worker)
        response.status == 200
        def workerId = response.data.id
        // when
        response = httpFindWorkerByUserId(worker.userId)
        // then
        assert response.status == 200
        def workerFromService = response.data
        assert workerFromService.id == workerId
        assert workerFromService.name == userNick
        assertLocation(workerFromService, location)
    }

    @Test
    void "should edit worker"() {
        // given
        def workerRequest = sampleWorker()
        mockUsers(userNick, location)
        def response = httpCreateWorker(workerRequest)
        assert response.status == 200
        def workerId = response.data.id
        def worker = new BaseWorkerDto(
                description: "new description",
                categoryIds: workerRequest.categoryIds,
                userId: workerRequest.userId)
        // when
        response = httpUpdateWorker(workerId, worker)
        // then
        assert response.status == 200
        def workerFromServer = httpGetWorker(workerId)
        // compare response to request
        def responseDetails = workerFromServer.data
        assertWorkers(responseDetails, worker.userId, worker.categoryIds, location)
        assert responseDetails.description == "new description"
    }

    def httpCreateWorker(worker) {
        httpClient().post(path: "/workers", body: worker, contentType: 'application/json', headers: ["Authorization": bearerToken])
    }

    def httpUpdateWorker(id, worker) {
        httpClient().put(path: "/workers/${id}", body: worker, contentType: 'application/json', headers: ["Authorization": bearerToken])
    }

    def httpGetWorker(id) {
        httpClient().get(path: "/workers/$id", contentType: 'application/json')
    }

    def httpFindWorker(categoryId, latitude, longitude) {
        httpClient().get(path: "/workers", query: ['categoryId': categoryId, "latitude": latitude, "longitude": longitude], contentType: 'application/json')
    }


    def httpFindWorkerByUserId(userId) {
        httpClient().get(path: "/workers/user/$userId", contentType: 'application/json')
    }

    BaseWorkerDto sampleWorker() {
        new BaseWorkerDto(
                userId: ownerId,
                description: "desc",
                categoryIds: ["1"])
    }

    def assertWorkers(workerToCheck, workerId, categoryIds, location){
        assert workerToCheck.userId == workerId
        assert workerToCheck.categoryIds == categoryIds
        assertLocation(workerToCheck, location)
    }

    def assertLocation(workerToCheck, location) {
        assert workerToCheck.location.title == location.title
        assert workerToCheck.location.latitude == location.latitude
        assert workerToCheck.location.longitude == location.longitude
    }
}
