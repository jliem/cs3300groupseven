package colab.common.channel.whiteboard.draw;

import java.awt.Color;

import junit.framework.TestCase;

/**
 * Test cases for {@link Path}.
 */
public class PathTester extends TestCase {

    /**
     * Creates a new Path, and checks that its
     * bounds are calculated correctly.
     */
    public void testGetBounds() {

        Path path = new Path(new Point(0, 0), Color.BLUE, 2);

        path.addPoint(new Point(14, 17));
        path.addPoint(new Point(18, 24));
        path.addPoint(new Point(62, -5));

        assertEquals(
                new java.awt.Rectangle(14, -5, 48, 29),
                path.getBounds()
        );

    }

}
