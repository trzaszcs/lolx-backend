package pl.poznan.lolx.domain

class SearchResult<T> {
    int totalCount
    List<T> items

    static SearchResult empty() {
        return new SearchResult(totalCount: 0, items: new ArrayList())
    }
}
