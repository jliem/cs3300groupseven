package colab.common.channel.whiteboard.draw;

import java.awt.Dimension;

/**
 * A simple rectangular shape.
 */
public class Rectangle extends Shape {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs an empty Rectangle.
     */
    public Rectangle() {
    }

    /**
     * Constructs a new Rectangle.
     *
     * @param size the size of the object
     * @param filled whether the object is filled with color
     */
    public Rectangle(final Dimension size, final boolean filled) {
        super(size, filled);
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Rectangle";
    }

}
