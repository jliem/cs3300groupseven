package colab.common.channel.whiteboard;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import junit.framework.TestCase;
import colab.common.channel.whiteboard.draw.Ellipse;
import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerIdentifier;

/**
 * Test cases for {@link Layer}.
 */
public class LayerTester extends TestCase {

    /**
     * Adds figures to the layer, and ensures that the content bounds
     * parameter is tracked properly.
     *
     * @throws Exception if any exception is thrown
     */
    public void testBounds() throws Exception {

        Layer layer = new Layer(new LayerIdentifier(4));

        layer.addFigure(new Ellipse(
            new Point(50, 50),
            new Dimension(25, 25)
        ));

        assertEquals(new Rectangle(50, 50, 25, 25), layer.getBounds());

        layer.addFigure(new Ellipse(
            new Point(100, 100),
            new Dimension(20, 40)
        ));

        assertEquals(new Rectangle(50, 50, 70, 90), layer.getBounds());

    }

    public void testCroppedImage() throws Exception {

        Layer layer = new Layer(new LayerIdentifier(4));

        layer.addFigure(new Ellipse(
            new Point(50, 50),
            new Dimension(25, 25)
        ));

        layer.addFigure(new Ellipse(
            new Point(100, 100),
            new Dimension(20, 40)
        ));

        BufferedImage croppedImage = layer.croppedImage();

        assertEquals(70, croppedImage.getWidth());
        assertEquals(90, croppedImage.getHeight());

    }

}
