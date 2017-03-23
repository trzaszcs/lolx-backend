package pl.poznan.lolx.rest.find

import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.SearchResult
import pl.poznan.lolx.rest.add.LocationDto

@Component
class SearchResultMapper {

    def map(SearchResult searchResult) {
        new SearchResultDto(
                totalCount: searchResult.totalCount,
                anounces: searchResult.items.collect {
                    new SimpleAnounceDto(
                            id: it.id,
                            title: it.title,
                            location: new LocationDto(title: it.location.title, latitude: it.location.latitude, longitude: it.location.longitude),
                            creationDate: it.creationDate,
                            price: it.price,
                            img: it.getSmallImage().orElse(null),
                            categoryName: it.category.name
                    )
                }
        )

    }

}
