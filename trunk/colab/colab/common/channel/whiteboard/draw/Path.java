package colab.common.channel.whiteboard.draw;

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

}
