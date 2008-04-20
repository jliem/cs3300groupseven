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
    @Override
    public Rectangle copy() {
        return new Rectangle(new Point(getPosition().x, getPosition().y),
                new Dimension(getSize().width, getSize().height),
                getColor(),
                getPenThickness(),
                getFilled());
    }

    /** {@inheritDoc} */
    @Override
    public void doDrawing(final Graphics g) {

        int positionX = getPosition().x;
        int positionY = getPosition().y;
        int width = (int) getSize().getWidth();
        int height = (int) getSize().getHeight();

        if (getFilled()) {
            g.fillRect(positionX, positionY, width, height);
        } else {
            g.drawRect(positionX, positionY, width, height);
        }

    }

}
