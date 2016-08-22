package pl.poznan.lolx.domain.add


interface UserDetails {
    Optional<User> find(String id)
}