package colab.common.channel.whiteboard.draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

import colab.common.channel.whiteboard.Drawable;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;
import colab.common.xml.XmlSerializable;

/**
 * A Drawable represents any kind of object drawn onto a layer.
 */
public abstract class Figure implements Drawable,
        Serializable, XmlSerializable {

    private Point position;

    private Color color;

    private int penThickness;

    /**
     * Constructs an empty Shape.
     */
    public Figure() {
    }

    /**
     * Constructs a new Shape.
     *
     * @param position the position of this shape on its layer
     */
    public Figure(final Point position, final Color color,
            final int penThickness) {

        this.position = position;
        this.color = color;
        this.penThickness = penThickness;
    }

    /** {@inheritDoc} */
    public void draw(final Graphics g) {

        ((Graphics2D) g).setPaint(color);
        ((Graphics2D) g).setStroke(new BasicStroke(penThickness));

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

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color
     */
    public void setColor(final Color color) {
        this.color = color;
    }

    /**
     * @return the pen thickness
     */
    public int getPenThickness() {
        return penThickness;
    }

    /**
     * @param penThickness the pen thickness
     */
    public void setPenThickness(final int penThickness) {
        this.penThickness = penThickness;
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
