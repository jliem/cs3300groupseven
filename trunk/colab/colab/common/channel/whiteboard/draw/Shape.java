package colab.common.channel.whiteboard.draw;

import java.awt.Color;
import java.awt.Dimension;

import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;

/**
 * A Shape is a drawable object which has a
 * width, a height, and optional fill.
 */
public abstract class Shape extends Figure {

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
     * @param position where to position this shape on a layer
     * @param size the width and height of the shape
     * @param color the color of the shape's fill or border
     * @param penThickness the thickness of the shape's border
     * @param filled whether the shape is filled with color
     */
    public Shape(final Point position, final Dimension size, final Color color,
            final int penThickness, final boolean filled) {

        super(position, color, penThickness);

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
