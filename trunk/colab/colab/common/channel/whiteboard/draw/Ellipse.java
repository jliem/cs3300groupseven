package colab.common.channel.whiteboard.draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * A simple elliptical shape.
 */
public class Ellipse extends Shape {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs an empty Ellipse.
     */
    public Ellipse() {
    }

    public Ellipse(final Point position,
            final Dimension size,
            final Color color,
            final int penThickness,
            final boolean filled) {

        super(position, size, color, penThickness, filled);
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Ellipse";
    }

    @Override
    protected void drawDrawable(final Graphics g) {
        // TODO Auto-generated method stub

    }

}
