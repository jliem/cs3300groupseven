package colab.common.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.AreaAveragingScaleFilter;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;

/**
 * Utility methods for dealing with images.
 */
public final class ImageUtils {

    /**
     * Hidden default constructor.
     */
    private ImageUtils() {
    }

    public static Image scaleToFit(final BufferedImage image,
            final Dimension maxSize, final Component component) {

        double maxRatio = maxSize.getWidth() / maxSize.getHeight();
        double imgRatio = ((double) image.getWidth()) / image.getHeight();

        double scaleRatio;
        if (maxRatio < imgRatio) {
            scaleRatio = maxSize.getWidth() / image.getWidth();
        } else {
            scaleRatio = maxSize.getHeight() / image.getHeight();
        }

        return scaleRatio(image, scaleRatio, component);

    }

    public static Image scaleRatio(final BufferedImage image,
            final double ratio, final Component component) {

        Dimension size = new Dimension((int) (image.getWidth() * ratio),
                                       (int) (image.getHeight() * ratio));

        return scaleExact(image, size, component);

    }

    public static Image scaleExact(final Image image, final Dimension size,
            final Component component) {

        ImageFilter filter =
            new AreaAveragingScaleFilter(size.width, size.height);

        ImageProducer producer =
            new FilteredImageSource(image.getSource(), filter);

        return component.createImage(producer);

    }

}
