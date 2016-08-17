package pl.poznan.lolx.domain


interface SearchEngine {
    SearchResult find(String phrase, int page, int itemsPerPage)
    SearchResult forUser(String userId, int page, int itemsPerPage)
    SearchResult getById(String anounceId)
    void index(Anounce anounce)
}