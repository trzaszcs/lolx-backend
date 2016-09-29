package pl.poznan.lolx.domain

import groovy.transform.ToString

@ToString
class Anounce {
    String id
    String title
    String description
    Location location
    Date creationDate = new Date()
    String ownerId
    String ownerName
    BigDecimal price
    String imgName



    final String imagePath = "/api/upload/"

    Optional<String> getSmallImage() {
        if (imgName) {
            def parts = imgName.split("\\.")
            def extension = parts[1]
            def fileName = parts[0]
            return Optional.of("$imagePath$fileName-256.$extension")
        }
        return Optional.empty()
    }

    Optional<String> getImage() {
        if (imgName) {
            return Optional.of("$imagePath$imgName")
        }
        return Optional.empty()
    }
}
