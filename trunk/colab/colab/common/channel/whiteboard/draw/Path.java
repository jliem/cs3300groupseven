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
    private Dimension size;

    /**
     * Constructs an empty Path.
     */
    public Path() {
    }

    /**
     * Constructs a new path.
     *
     * @param position the position of this shape on its layer
     * @param color
     * @param penThickness
     */
    public Path(final Point position, final Color color,
            final int penThickness) {
        super(position, color, penThickness);
        clearPoints();
    }

    /**
     * Adds a point to this path.
     * @param point the point to add
     */
    public void addPoint(final Point point) {

        if (point.x > size.width + getPosition().x) {
            size.width = point.x;
        }

        if (point.y > size.height + getPosition().y) {
            size.height = point.y;
        }

        points.add(point);

    }

    /**
     * @return the list of points, in order
     */
    public List<Point> getPoints() {
        return points;
    }

    private void clearPoints() {
        this.points = new ArrayList<Point>();
        this.size = new Dimension(0, 0);
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

        points.clear();
        for (XmlNode child : node.getChildren()) {
            Point point = new Point();
            point.fromXml(child);
            this.points.add(point);
        }

    }

    /** {@inheritDoc} */
    @Override
    public void doDrawing(final Graphics g) {

        for (int i = 0; i < points.size() - 1; i++) {
            Point one = points.get(i);
            Point two = points.get(i+1);
            if (containsLine(g.getClipBounds(), one, two)) {
                g.drawLine(one.x, one.y, two.x, two.y);
            }
        }

    }

    private boolean containsLine(final java.awt.Rectangle rectangle,
            final Point one, final Point two) {

        return rectangle.contains(one) || rectangle.contains(two);

    }

    /** {@inheritDoc} */
    @Override
    public Dimension getSize() {
        return size;
    }

}
