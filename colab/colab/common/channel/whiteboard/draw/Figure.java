package colab.common.channel.whiteboard.draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

import colab.common.DebugManager;
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
     * @param color the color of the figure's fill or border
     * @param penThickness the thickness of the figure's border
     */
    public Figure(final Point position, final Color color,
            final int penThickness) {

        this.position = position;
        this.color = color;
        this.penThickness = penThickness;
    }

    /**
     * Constructs a new Figure from an xml node.
     *
     * @param node the node containing the data
     * @return a Figure constructed from the given data
     * @throws XmlParseException if the xml node is improperly formatted
     */
    public static Figure constructFromXml(final XmlNode node)
            throws XmlParseException {

        Figure figure = instantiateFromXmlType(node.getType());

        figure.fromXml(node);

        return figure;

    }

    private static Figure instantiateFromXmlType(
            final String type) throws XmlParseException {

        Figure data;

        data = new Ellipse();
        if (type.equals(data.xmlNodeName())) {
            return data;
        }

        data = new Path();
        if (type.equals(data.xmlNodeName())) {
            return data;
        }

        data = new Rectangle();
        if (type.equals(data.xmlNodeName())) {
            return data;
        }

        throw new XmlParseException();

    }

    /** {@inheritDoc} */
    public final void draw(final Graphics g) {

        //DebugManager.debug("Drawing a " + xmlNodeName() + "...");

        ((Graphics2D) g).setPaint(color);
        ((Graphics2D) g).setStroke(new BasicStroke(penThickness));

        if (inClippedRegion(g)) {
            doDrawing(g);
        }

    }

    /**
     * Actually performs the function of draw().
     *
     * @param g the graphics object to draw onto
     */
    protected abstract void doDrawing(final Graphics g);

    /**
     * @return the dimensions of the figure
     */
    abstract Dimension getSize();

    /**
     * @return a rectangle which spans the bounds of this figure on a layer
     */
    public java.awt.Rectangle getBounds() {
        return new java.awt.Rectangle(getPosition(), getSize());
    }

    /**
     * Determines whether the figure is within the clipping bounds of
     * a graphics object, to decide whether it needs to be painted.
     *
     * @param graphics the graphics object
     * @return true if any portion of the figure is visible
     *         in the clipped region, false otherwise
     */
    protected boolean inClippedRegion(final Graphics graphics) {
        java.awt.Rectangle clipBounds = graphics.getClipBounds();
        if (clipBounds == null) {
            return true;
        }
        return clipBounds.intersects(getBounds());
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
     * @return a deep copy of this figure
     */
    public abstract Figure copy();

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

        node.setAttribute("red", Integer.toString(color.getRed()));
        node.setAttribute("green", Integer.toString(color.getGreen()));
        node.setAttribute("blue", Integer.toString(color.getBlue()));

        node.setAttribute("penThickness", Integer.toString(penThickness));

        return node;

    }

    /** {@inheritDoc} */
    public void fromXml(final XmlNode node) throws XmlParseException {

        int x, y, red, green, blue;
        try {

            x = Integer.parseInt(node.getAttribute("posX"));
            y = Integer.parseInt(node.getAttribute("posY"));

            red = Integer.parseInt(node.getAttribute("red"));
            green = Integer.parseInt(node.getAttribute("green"));
            blue = Integer.parseInt(node.getAttribute("blue"));

            this.penThickness = Integer.parseInt(
                    node.getAttribute("penThickness"));

        } catch (final NumberFormatException e) {
            throw new XmlParseException(e);

        }

        this.position = new Point(x, y);

        this.color = new Color(red, green, blue);

    }

}
