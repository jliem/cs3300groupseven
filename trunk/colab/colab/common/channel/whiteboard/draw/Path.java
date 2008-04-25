package colab.common.channel.whiteboard.draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;

/**
 * A path consists of a series of connected points.
 */
public class Path extends Figure {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private List<Point> points;

    /** Derived from points list. */
    private java.awt.Rectangle bounds;

    /**
     * Constructs an empty Path.
     */
    public Path() {
    }

    /**
     * Constructs a new path.
     *
     * @param position the position of this shape on its layer
     * @param color the color of the path
     * @param penThickness the thickness of the path
     */
    public Path(final Point position, final Color color,
            final int penThickness) {
        super(position, color, penThickness);
        clearPoints();
    }

    /**
     * Constructs a new path.
     *
     * @param a the first point in the path
     * @param b the second point in the path
     * @param color the color of the path
     * @param penThickness the thickness of the path
     */
    public Path(final java.awt.Point a, final java.awt.Point b,
            final Color color, final int penThickness) {
        clearPoints();
        setPosition(new Point(0, 0));
        addPoint(a);
        addPoint(b);
        setColor(color);
        setPenThickness(penThickness);
    }

    /**
     * Adds a point to this path.
     * @param point the point to add
     */
    public void addPoint(final java.awt.Point point) {

        if (points.size() == 1) {
            Point a = new Point(
                    Math.min(points.get(0).x, point.x),
                    Math.min(points.get(0).y, point.y));
            Dimension d = new Dimension(
                    Math.abs(points.get(0).x - point.x),
                    Math.abs(points.get(0).y - point.y));
            bounds.setBounds(new java.awt.Rectangle(a, d));
        } else {
            bounds.add(point);
        }

        points.add(new Point(point));

    }

    /**
     * @return the list of points, in order
     */
    public List<Point> getPoints() {
        return points;
    }

    /** {@inheritDoc} */
    @Override
    public Figure copy() {
        Path path = new Path(new Point(getPosition().x, getPosition().y),
                getColor(), getPenThickness());

        for(Point p : points) {
            path.addPoint(p);
        }

        return path;
    }

    private void clearPoints() {
        this.points = new ArrayList<Point>();
        this.bounds = new java.awt.Rectangle(0, 0, 0, 0);
    }

    /**
     * @param points the list of points, in order
     */
    public void setPoints(final List<Point> points) {
        clearPoints();
        for (Point point : points) {
            addPoint(point);
        }
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Path";
    }

    /** {@inheritDoc} */
    @Override
    public XmlNode toXml() {

        XmlNode node = super.toXml();

        for (Point point : this.points) {
            XmlNode child = point.toXml();
            node.addChild(child);
        }

        return node;

    }

    /** {@inheritDoc} */
    @Override
    public void fromXml(final XmlNode node) throws XmlParseException {

        super.fromXml(node);

        clearPoints();

        for (XmlNode child : node.getChildren()) {
            Point point = new Point();
            point.fromXml(child);
            addPoint(point);
        }

    }

    /** {@inheritDoc} */
    @Override
    public void doDrawing(final Graphics g) {

        for (int i = 0; i < points.size() - 1; i++) {
            java.awt.Point one = new java.awt.Point(points.get(i));
            java.awt.Point two = new java.awt.Point(points.get(i+1));
            // Should only draw the lines which fall into the clip rectangle
            g.drawLine(one.x, one.y, two.x, two.y);
        }

    }

    /** {@inheritDoc} */
    @Override
    public Dimension getSize() {
        return bounds.getSize();
    }

    /** {@inheritDoc} */
    @Override
    public java.awt.Rectangle getBounds() {
        return bounds;
    }

}
