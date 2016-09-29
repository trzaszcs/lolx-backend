package pl.poznan.lolx.domain.upload

import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

@Component
@Slf4j
class UploadHandler {

    final static int SMALL = 256

    String save(String fileExtension, byte[] content) {
        def generatedFileName = generateFileName()
        File file = file(generatedFileName + "." + fileExtension)
        file.bytes = content
        log.info("saved {} image", file.name)
        scaleImage(file, generatedFileName, fileExtension, SMALL)
        return file.getName()
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

    def scaleImage(imgFile, fileName, ext, width) {
        BufferedImage img = ImageIO.read(imgFile)
        int newHeight, newWidth
        if (img.getWidth() > width) {
            int scaleRatio = img.getWidth() / width;
            newHeight = img.getHeight() / scaleRatio;
            newWidth = width
        } else {
            newWidth = img.getWidth()
            newHeight = img.getHeight()
        }

        BufferedImage dimg = new BufferedImage(newWidth, newHeight, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, width, newHeight, 0, 0, img.width, img.height, null);
        g.dispose();
        File scaledImage = file("$fileName-$width.$ext")
        ImageIO.write(dimg, "png", scaledImage)
        log.info("saved scaled {} image {}", width, scaledImage.name)
    }

    def file(fileName){
        new File(System.getProperty("java.io.tmpdir") + File.separator + fileName)
    }

}
