package colab.common.channel.whiteboard.draw;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;

/**
 * A path consists of a series of connected points.
 */
public class Path extends Drawable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private List<Point> points;

    /**
     * Constructs a new Path.
     */
    public Path() {
        points = new ArrayList<Point>();
    }

    /**
     * Adds a point to this path.
     * @param point the point to add
     */
    public void addPoint(final Point point) {
        points.add(point);
    }

    /**
     * @return the list of points, in order
     */
    public List<Point> getPoints() {
        return points;
    }

    /**
     * @param points the list of points, in order
     */
    public void setPoints(final List<Point> points) {
        this.points = points;
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
    protected void drawDrawable(Graphics g) {

        for (int i=0; i<points.size()-1; i++) {
            Point one = points.get(i);
            Point two = points.get(i+1);

            g.drawLine(one.x, one.y, two.x, two.y);
        }


    }

}
