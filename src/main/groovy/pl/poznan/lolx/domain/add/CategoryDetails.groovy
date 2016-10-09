package pl.poznan.lolx.domain.add


interface CategoryDetails {
    Optional<Category> find(String id)
}