package pl.poznan.lolx.domain.requestOrder


interface RequestOrderDao {
    Optional<RequestOrder> findById(String id)

    String save(RequestOrder requestOrder)

    boolean accept(String id)

    boolean remove(String id, String authorId)

    List<RequestOrder> findByAnounceId(String anounceId)

    Optional<RequestOrder> findByAnounceIdAndAuthorId(String anounceId, String authorId)
}