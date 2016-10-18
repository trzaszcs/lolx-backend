package pl.poznan.lolx.domain

interface AnounceDao {
    void save(Anounce anounce)
    void update(Anounce anounce)
}