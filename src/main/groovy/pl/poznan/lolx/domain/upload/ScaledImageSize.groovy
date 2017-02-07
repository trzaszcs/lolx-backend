package pl.poznan.lolx.domain.upload


enum ScaledImageSize {

    SMALL(256), MEDIUM(512);

    final int width

    ScaledImageSize(int width) {
        this.width = width
    }

    String getFileName(String originalFileName) {
        String[] imageParts = originalFileName.split("\\.")
        return getFileName(imageParts[0], imageParts[1])
    }

    String getFileName(String fileName, String ext) {
        return "${fileName}-${this.name()}.${ext}"
    }
}