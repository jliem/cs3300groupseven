package colab.common.channel.whiteboard.draw;

import java.awt.Point;
import java.io.Serializable;

import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;
import colab.common.xml.XmlSerializable;

/**
 * A Drawable represents any kind of object drawn onto a layer.
 */
public abstract class Drawable implements Serializable, XmlSerializable {

    private Point position;

    /**
     * Constructs an empty Shape.
     */
    public Drawable() {
    }

    /**
     * Constructs a new Shape.
     *
     * @param position the position of this shape on its layer
     */
    public Drawable(final Point position) {
        this.position = position;
    }

    /**
     * @return the position of this shape on its layer
     */
    public Point getPosition() {
        return position;
    }

    /**
     * @param position the position of this shape on its layer
     */
    public void setPosition(final Point position) {
        this.position = position;
    }

    /** {@inheritDoc} */
    public XmlNode toXml() {

        XmlNode node = new XmlNode(xmlNodeName());

        node.setAttribute("posX", Integer.toString(position.x));
        node.setAttribute("posY", Integer.toString(position.y));

        return node;

    }

    /** {@inheritDoc} */
    public void fromXml(final XmlNode node) throws XmlParseException {

        int x, y;
        try {
            x = Integer.parseInt(node.getAttribute("posX"));
            y = Integer.parseInt(node.getAttribute("posY"));
        } catch (final NumberFormatException e) {
            throw new XmlParseException(e);
        }
        this.position = new Point(x, y);

    }

}
