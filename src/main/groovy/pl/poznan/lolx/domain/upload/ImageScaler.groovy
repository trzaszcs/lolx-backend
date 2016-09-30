package pl.poznan.lolx.domain.upload

import groovy.transform.PackageScope
import org.springframework.stereotype.Component

import java.awt.*
import java.awt.image.BufferedImage

@PackageScope
@Component
class ImageScaler {

    BufferedImage scaleImage(BufferedImage img, ScaledImageSize imageSize) {
        int newHeight, newWidth, width = imageSize.width
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
        g.dispose()

        return dimg
    }

}
