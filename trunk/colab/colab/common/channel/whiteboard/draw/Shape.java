package colab.common.channel.whiteboard.draw;

import java.awt.Dimension;

import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;

/**
 * A Shape is a drawable object which has a
 * width, a height, and optional fill.
 */
public abstract class Shape extends Drawable {

    private Dimension size;

    private boolean filled;

    /**
     * Constructs an empty Shape.
     */
    public Shape() {
    }

    /**
     * Constructs a new Shape.
     *
     * @param size the size of the object
     * @param filled whether the rectangle is filled with color
     */
    public Shape(final Dimension size, final boolean filled) {
        this.size = size;
        this.filled = filled;
    }

    /**
     * @return the size of the object
     */
    public Dimension getSize() {
        return size;
    }

    /**
     * @param size the size of the object
     */
    public void setSize(final Dimension size) {
        this.size = size;
    }

    /**
     * @return whether the object is filled with color
     */
    public boolean getFilled() {
        return filled;
    }

    /**
     * @param filled whether the object is filled with color
     */
    public void setFilled(final boolean filled) {
        this.filled = filled;
    }

    /** {@inheritDoc} */
    @Override
    public XmlNode toXml() {

        XmlNode node = super.toXml();

        node.setAttribute("width", Integer.toString((int) size.width));
        node.setAttribute("height", Integer.toString((int) size.height));

        node.setAttribute("filled", Boolean.toString(filled));

        return node;

    }

    /** {@inheritDoc} */
    @Override
    public void fromXml(final XmlNode node) throws XmlParseException {

        super.fromXml(node);

        int width, height;
        try {
            width = Integer.parseInt(node.getAttribute("width"));
            height = Integer.parseInt(node.getAttribute("height"));
        } catch (final NumberFormatException e) {
            throw new XmlParseException(e);
        }
        this.size = new Dimension(width, height);

        this.filled = Boolean.parseBoolean(node.getAttribute("filled"));

    }

}
