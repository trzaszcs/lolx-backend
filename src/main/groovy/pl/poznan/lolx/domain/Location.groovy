package pl.poznan.lolx.domain

class Location extends Coordinate {
    final String title

    Location(String title, double latitude, double longitude) {
        super(latitude, longitude)
        this.title = title
    }
}
