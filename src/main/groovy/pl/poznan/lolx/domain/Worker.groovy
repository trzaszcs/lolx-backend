package pl.poznan.lolx.domain

import groovy.transform.ToString

@ToString
class Worker {
    final String id
    final String userId
    final String description
    final String photoUrl
    final Location location
    final List<String> categories
    final String name

    public Worker(String id, String userId, String description, String photoUrl, List<String> categories, Location location, String name) {
        assert userId && !userId.isEmpty()
        assert categories && !categories.isEmpty()
        assert location != null
        this.id = id
        this.userId = userId
        this.description = description
        this.photoUrl = photoUrl
        this.categories = Collections.unmodifiableList(categories)
        this.location = location
        this.name = name
    }

    Worker withId(String id) {
        new Worker(id, this.userId, this.description, this.photoUrl, this.categories, this.location, this.name)
    }


    Worker update(String description, List<String> categories) {
        new Worker(this.id, this.userId, description, this.photoUrl, categories, location, name)
    }

    Optional<String> getPhotoUrl() {
        return Optional.ofNullable(photoUrl)
    }

    String getLocationTitle() {
        location.title
    }

    double getLatitude() {
        location.latitude
    }

    double getLongitude() {
        location.longitude
    }
}