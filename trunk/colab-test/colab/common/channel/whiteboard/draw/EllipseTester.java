package colab.common.channel.whiteboard.draw;

import java.awt.Dimension;

import junit.framework.TestCase;

/**
 * Test cases for {@link Ellipse}.
 */
public class EllipseTester extends TestCase {

    /**
     * Creates a new Ellipse, an checks that its
     * bounds are calculated correctly.
     */
    public void testGetBounds() {

        Ellipse ellipse = new Ellipse(
            new Point(12, 17),
            new Dimension(34, 81)
        );

        assertEquals(
                new java.awt.Rectangle(12, 17, 34, 81),
                ellipse.getBounds());

    }

}
