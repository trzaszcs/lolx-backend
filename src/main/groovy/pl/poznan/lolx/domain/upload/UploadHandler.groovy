package pl.poznan.lolx.domain.upload

import org.springframework.stereotype.Component

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

@Component
class UploadHandler {

    String save(String fileExtension, byte[] content) {
        def generatedFileName = generateFileName()
        File file = File.createTempFile(generatedFileName, ".$fileExtension")
        file.bytes = content
        scaleImage(file, generatedFileName, fileExtension, 256)
        return file.getName()
    }

    Optional<File> getFile(String fileName) {
        def f = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName)
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
        File file = File.createTempFile(fileName + "-" + width, ".$ext")
        ImageIO.write(dimg, "png", file)
    }

}
