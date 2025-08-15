package io.printkit.processing.raster;

import processing.core.PImage;
import java.awt.image.BufferedImage;

final class ImageUtil {
    private ImageUtil() {
    }

    static BufferedImage toBufferedImage(PImage pimg) {
        pimg.loadPixels();

        int w = pimg.width;
        int h = pimg.height;
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        out.setRGB(0, 0, w, h, pimg.pixels, 0, w);
        return out;
    }
}
