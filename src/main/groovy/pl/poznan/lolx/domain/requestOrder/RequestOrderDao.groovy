package pl.poznan.lolx.domain.requestOrder


interface RequestOrderDao {
    Optional<RequestOrder> findById(String id)

    String save(RequestOrder requestOrder)

    boolean accept(String id)

    boolean reject(String id)

    boolean remove(String id, String authorId)

    List<RequestOrder> findByAnounceId(String anounceId)

    Optional<RequestOrder> findByAnounceIdAndAuthorId(String anounceId, String authorId)

    Optional<RequestOrder> findByIdAndAuthorId(String id, String authorId)

    List<RequestOrder> findByAuthorId(String authorId)

    List<RequestOrder> findByAnounceAuthorId(String anounceAuthorId)

    List<RequestOrder> findByAnounceAuthorIdOrAuthorId(String authorId)

}