package colab.common.channel.whiteboard.draw;

import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;
import colab.common.xml.XmlSerializable;

/**
 * An extension of the AWT Point class which supports XML serialization.
 */
public class Point extends java.awt.Point implements XmlSerializable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs an empty Point.
     */
    public Point() {
    }

    /**
     * Constructs a new point.
     *
     * @param x the horizontal position
     * @param y the vertical position
     */
    public Point(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new Point.
     *
     * @param point an AWT point
     */
    public Point(final java.awt.Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Point";
    }

    /** {@inheritDoc} */
    public XmlNode toXml() {

        XmlNode node = new XmlNode(xmlNodeName());

        node.setAttribute("x", Integer.toString(x));
        node.setAttribute("y", Integer.toString(y));

        return node;

    }

    /** {@inheritDoc} */
    public void fromXml(final XmlNode node) throws XmlParseException {

        try {
            this.x = Integer.parseInt(node.getAttribute("x"));
            this.y = Integer.parseInt(node.getAttribute("y"));
        } catch (final NumberFormatException e) {
            throw new XmlParseException(e);
        }

    }

}
