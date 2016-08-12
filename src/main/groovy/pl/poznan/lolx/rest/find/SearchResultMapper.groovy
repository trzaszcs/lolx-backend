package pl.poznan.lolx.rest.find

import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.SearchResult

@Component
class SearchResultMapper {

    def map(SearchResult searchResult) {
        new SearchResultDto(
                totalCount: searchResult.totalCount,
                anounces: searchResult.anounces.collect {
                    new SimpleAnounceDto(id: it.id, title: it.title, state: it.state, city: it.city)
                }
        )

    }

}
