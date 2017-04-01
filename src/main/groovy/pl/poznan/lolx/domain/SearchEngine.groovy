package pl.poznan.lolx.domain


interface SearchEngine {
    SearchResult<Anounce> find(String phrase, AnounceType type, int page, int itemsPerPage, Optional<Coordinate> coordinate, Optional<String> categoryId)
    SearchResult<Anounce> forUser(String userId, int page, int itemsPerPage)
    Optional<Anounce> getById(String anounceId)
    void index(Anounce anounce)
    void delete(String indexId)

    SearchResult<Worker> findWorkers(String categoryId, Coordinate coordinate, int page, int itemsPerPage)
    void index(Worker worker)
    void deleteWorker(String id)
}