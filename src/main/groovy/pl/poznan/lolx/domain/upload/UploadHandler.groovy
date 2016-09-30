package pl.poznan.lolx.domain.upload

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

@Component
@Slf4j
class UploadHandler {

    @Autowired
    ImageScaler imageScaler

    String save(String fileExtension, InputStream stream) {
        def generatedFileName = generateFileName()
        def baseName = "$generatedFileName.$fileExtension"
        log.debug("saved {} image", baseName)
        BufferedImage img = ImageIO.read(stream)
        scaleImage(img, generatedFileName, fileExtension, ScaledImageSize.SMALL)
        scaleImage(img, generatedFileName, fileExtension, ScaledImageSize.MEDIUM)
        return baseName
    }

    Optional<File> getFile(String fileName) {
        def f = file(fileName)
        if (f.exists()) {
            return Optional.of(f)
        } else {
            return Optional.empty()
        }
    }

    def generateFileName() {
        UUID.randomUUID().toString()
    }

    def scaleImage(BufferedImage bufferedImage, String fileName, String ext, ScaledImageSize imageSize) {
        BufferedImage dimg = imageScaler.scaleImage(bufferedImage, imageSize)
        File scaledImage = file(imageSize.getFileName(fileName, ext))
        ImageIO.write(dimg, "png", scaledImage)
        log.debug("saved scaled {} image {}", imageSize, scaledImage.name)
    }

    def file(fileName) {
        new File(System.getProperty("java.io.tmpdir") + File.separator + fileName)
    }

}
