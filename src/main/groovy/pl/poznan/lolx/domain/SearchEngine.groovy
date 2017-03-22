package pl.poznan.lolx.domain


interface SearchEngine {
    SearchResult<Anounce> find(String phrase, AnounceType type, int page, int itemsPerPage, Coordinate coordinate, String categoryId)
    SearchResult<Anounce> forUser(String userId, int page, int itemsPerPage)
    Optional<Anounce> getById(String anounceId)
    void index(Anounce anounce)
    void delete(String indexId)
}