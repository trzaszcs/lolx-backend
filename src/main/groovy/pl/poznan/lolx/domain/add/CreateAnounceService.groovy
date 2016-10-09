package pl.poznan.lolx.domain.add

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.Anounce
import pl.poznan.lolx.domain.AnounceDao
import pl.poznan.lolx.domain.SearchEngine
import rx.Observable
import rx.schedulers.Schedulers

import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Component
class CreateAnounceService {

    @Autowired
    AnounceDao anounceDao
    @Autowired
    SearchEngine searchEngine
    @Autowired
    UserDetails userDetails
    @Autowired
    CategoryDetails categoryDetails

    Executor executor = Executors.newFixedThreadPool(10)

    String create(AnounceCreationRequest anounceRequest) {
        def anounceId = genId()

        def optCategory = categoryDetails.find(anounceRequest.categoryId)

        if (!optCategory.isPresent()) {
            throw new RuntimeException("Category with id ${anounceRequest.categoryId} not found")
        }

        def anounce = new Anounce(
                id: anounceId,
                title: anounceRequest.title,
                description: anounceRequest.description,
                location: anounceRequest.location,
                ownerId: anounceRequest.ownerId,
                ownerName: userDetails.find(anounceRequest.ownerId),
                price: anounceRequest.price,
                imgName: anounceRequest.imgName,
                category: optCategory.get()
        )
        searchEngine.index(anounce)
        anounceDao.save(anounce)
        return anounceId
    }

    def genId() {
        UUID.randomUUID().toString()
    }

    def details(anounceRequest){
        def userObs = Observable.<Optional<User>>create({
            it.onNext(userDetails.find(anounceRequest.ownerId))
            it.onCompleted()
        })

        def categoryObs = Observable.<Optional<Category>>create({
            it.onNext(categoryDetails.find(anounceRequest.categoryId))
            it.onCompleted()
        })

        return Observable.zip(userObs, categoryObs, { user , category ->
            new Details(user: user, category: category)
        }).observeOn(Schedulers.io()).toBlocking().first()

    }

    class Details {
        Optional<User> user
        Optional<Category> category
    }
}
