package colab.common.channel.whiteboard.draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

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
    public Rectangle(final Point position,
            final Dimension size,
            final Color color,
            final int penThickness,
            final boolean filled) {

        super(position, size, color, penThickness, filled);
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Rectangle";
    }

    /** {@inheritDoc} */
    protected void drawDrawable(final Graphics g) {
        if (getFilled()) {
            g.fillRect(getPosition().x,
                       getPosition().y,
                       (int) getSize().getWidth(),
                       (int) getSize().getHeight());
        } else {
            g.drawRect(getPosition().x,
                       getPosition().y,
                       (int) getSize().getWidth(),
                       (int) getSize().getHeight());
        }
    }

}
