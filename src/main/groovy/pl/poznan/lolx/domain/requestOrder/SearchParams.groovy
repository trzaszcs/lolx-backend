package pl.poznan.lolx.domain.requestOrder

class SearchParams {
    String authorId
    Status status
    int itemsPerPage
    int page

    SearchParams(String authorId, Status status, int itemsPerPage, int page) {
        assert authorId
        assert itemsPerPage > 0 && itemsPerPage <= 20
        assert page >= 0

        this.authorId = authorId
        this.status = status
        this.itemsPerPage = itemsPerPage
        this.page = page
    }

    boolean hasStatusFilter() {
        status != null
    }
}
