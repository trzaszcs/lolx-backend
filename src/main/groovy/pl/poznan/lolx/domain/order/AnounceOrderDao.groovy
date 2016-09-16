package pl.poznan.lolx.domain.order

interface AnounceOrderDao {
    void order(AnounceOrder anounceOrder)
    AnounceOrder get(String id)
    List<AnounceOrder> getByCustomerId(String id)
}