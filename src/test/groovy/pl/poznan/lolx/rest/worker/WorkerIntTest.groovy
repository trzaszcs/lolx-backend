package pl.poznan.lolx.rest.worker

import org.junit.Test
import pl.poznan.lolx.rest.IntTest
import pl.poznan.lolx.rest.add.LocationDto

class WorkerIntTest extends IntTest {


    @Test
    void "should add worker"() {
        // given
        def worker = sampleWorker()
        mockUsers()
        // when
        def response = httpCreateWorker(worker)
        // then
        assert response.status == 200
        def workerId = response.data.id
        assert workerId
        def workerFromServer = httpGetWorker(workerId)
        // compare response to request
        def responseDetails = workerFromServer.data
        assert responseDetails.userId == worker.userId
        assert responseDetails.categoryIds == worker.categoryIds
        assert responseDetails.photoUrl == worker.photoUrl
        assert responseDetails.location.title == worker.location.title
        assert responseDetails.location.latitude == worker.location.latitude
        assert responseDetails.location.longitude == worker.location.longitude
    }

    @Test
    void "should find added worker"() {
        // given
        def worker = sampleWorker()
        def userNick = "someNick"
        mockUsers(userNick)
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
        assert foundWorker.photoUrl == worker.photoUrl
        assert foundWorker.location.title == worker.location.title
        assert foundWorker.location.latitude == worker.location.latitude
        assert foundWorker.location.longitude == worker.location.longitude
    }

    @Test
    void "should edit worker"() {
        // given
        def worker = sampleWorker()
        mockUsers()
        def response = httpCreateWorker(worker)
        assert response.status == 200
        worker.id = response.data.id
        worker.description = "new description"
        // when
        response = httpUpdateWorker(worker)
        // then
        assert response.status == 200
        def workerFromServer = httpGetWorker(worker.id)
        // compare response to request
        def responseDetails = workerFromServer.data
        assert responseDetails.userId == worker.userId
        assert responseDetails.description == "new description"
        assert responseDetails.categoryIds == worker.categoryIds
        assert responseDetails.photoUrl == worker.photoUrl
        assert responseDetails.location.title == worker.location.title
        assert responseDetails.location.latitude == worker.location.latitude
        assert responseDetails.location.longitude == worker.location.longitude
    }

    def httpCreateWorker(worker) {
        httpClient().post(path: "/workers", body: worker, contentType: 'application/json', headers: ["Authorization": bearerToken])
    }

    def httpUpdateWorker(worker) {
        httpClient().put(path: "/workers/${worker.id}", body: worker, contentType: 'application/json', headers: ["Authorization": bearerToken])
    }

    def httpGetWorker(id) {
        httpClient().get(path: "/workers/$id", contentType: 'application/json')
    }

    def httpFindWorker(categoryId, latitude, longitude) {
        httpClient().get(path: "/workers", query: ['categoryId': categoryId, "latitude": latitude, "longitude": longitude], contentType: 'application/json')
    }

    WorkerDto sampleWorker() {
        new WorkerDto(
                userId: ownerId,
                description: "desc",
                categoryIds: ["1"],
                photoUrl: "",
                location: new LocationDto(title: "Poznan", latitude: 22.1d, longitude: 22.3d))
    }
}
