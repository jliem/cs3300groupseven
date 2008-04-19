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
    /** {@inheritDoc} */
    public void draw(final Graphics g) {

        super.draw(g);

        int positionX = getPosition().x;
        int positionY = getPosition().y;
        int width = (int) getSize().getWidth();
        int height = (int) getSize().getHeight();

        if (getFilled()) {
            g.fillOval(positionX, positionY, width, height);
        } else {
            g.drawOval(positionX, positionY, width, height);
        }

    }

}
