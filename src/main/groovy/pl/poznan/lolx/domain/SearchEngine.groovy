package pl.poznan.lolx.domain


interface SearchEngine {
    SearchResult find(String phrase, int page, int itemsPerPage, Optional<Coordinate> coordinate, Optional<String> categoryId)
    SearchResult forUser(String userId, int page, int itemsPerPage)
    Optional<Anounce> getById(String anounceId)
    void index(Anounce anounce)
}