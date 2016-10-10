package pl.poznan.lolx.domain.add


class UserNotFoundException extends RuntimeException {
    UserNotFoundException(String cause) {
        super(cause)
    }
}
