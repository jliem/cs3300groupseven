package colab.common.channel.whiteboard.draw;

import java.awt.Dimension;

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

    /**
     * Constructs a new Ellipse.
     *
     * @param size the size of the object
     * @param filled whether the rectangle is filled with color
     */
    public Ellipse(final Dimension size, final boolean filled) {
        super(size, filled);
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Ellipse";
    }

}
