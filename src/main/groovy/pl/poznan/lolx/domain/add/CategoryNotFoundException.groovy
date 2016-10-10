package pl.poznan.lolx.domain.add


class CategoryNotFoundException extends RuntimeException {
    CategoryNotFoundException(String cause) {
        super(cause)
    }
}
