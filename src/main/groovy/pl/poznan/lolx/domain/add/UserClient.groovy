package pl.poznan.lolx.domain.add


interface UserClient {
    Optional<User> find(String id, boolean detailed)

    Map<String, User> find(List<String> ids)
}