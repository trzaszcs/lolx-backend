package pl.poznan.lolx.domain

class Location {
    final String title
    final double latitude
    final double longitude

    Location(String title, double latitude, double longitude) {
        this.title = title
        this.latitude = latitude
        this.longitude = longitude
    }
}
