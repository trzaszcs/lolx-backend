package pl.poznan.lolx.domain

import groovy.transform.ToString
import pl.poznan.lolx.domain.add.Category
import pl.poznan.lolx.domain.upload.ScaledImageSize

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
    Category category
    AnounceType type
    AnounceDuration duration
    boolean closed
    String contactPhone

    final String imagePath = "/api/upload/"

    Optional<String> getSmallImage() {
        return getImage(ScaledImageSize.SMALL)
    }

    Optional<String> getImage() {
        return getImage(ScaledImageSize.MEDIUM)
    }

    Optional<String> getImage(ScaledImageSize scaledImageSize) {
        if (imgName) {
            def scaledFileName = scaledImageSize.getFileName(imgName)
            return Optional.of("$imagePath$scaledFileName")
        }
        return Optional.empty()
    }

    void markAsClosed() {
        closed = true
    }
}
