package pl.poznan.lolx.domain.add

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.Anounce
import pl.poznan.lolx.domain.AnounceDao
import pl.poznan.lolx.domain.SearchEngine
import rx.Observable
import rx.Scheduler
import rx.schedulers.Schedulers

import java.util.concurrent.Executors

@Component
class CreateAnounceService {

    @Autowired
    AnounceDao anounceDao
    @Autowired
    SearchEngine searchEngine
    @Autowired
    UserClient userDetails
    @Autowired
    CategoryDetails categoryDetails
    final Scheduler scheduler = Schedulers.from(Executors.newFixedThreadPool(10))

    String create(AnounceCreationRequest anounceRequest) {
        def anounceId = genId()

        def details = details(anounceRequest)

        def anounce = new Anounce(
                id: anounceId,
                title: anounceRequest.title,
                description: anounceRequest.description,
                location: anounceRequest.location,
                ownerId: anounceRequest.ownerId,
                ownerName: details.getUser().orElseThrow({
                    new UserNotFoundException("User with id ${anounceRequest.ownerId} not found")
                }).name(),
                price: anounceRequest.price,
                imgName: anounceRequest.imgName,
                category: details.getCategory().orElseThrow({
                    new CategoryNotFoundException("Category with id ${anounceRequest.categoryId} not found")
                }),
                type: anounceRequest.type,
                duration: anounceRequest.duration
        )
        searchEngine.index(anounce)
        anounceDao.save(anounce)
        return anounceId
    }

    def genId() {
        UUID.randomUUID().toString()
    }

    def details(anounceRequest) {
        def userObs = Observable.<Optional<User>> create({
            it.onNext(userDetails.find(anounceRequest.ownerId, false))
            it.onCompleted()
        }).subscribeOn(scheduler)

        def categoryObs = Observable.<Optional<Category>> create({
            it.onNext(categoryDetails.find(anounceRequest.categoryId))
            it.onCompleted()
        }).subscribeOn(scheduler)

        return Observable.zip(userObs, categoryObs, { user, category ->
            new Details(user.orElse(null), category.orElse(null))
        }).toBlocking().first()

    }

    private class Details {
        final User user
        final Category category

        Details(User user, Category category) {
            this.user = user
            this.category = category
        }

        Optional<User> getUser() {
            Optional.ofNullable(user)
        }

        Optional<Category> getCategory() {
            Optional.ofNullable(category)
        }
    }
}
