package colab.common.channel.whiteboard.shape;

import java.awt.Point;
import java.util.List;

public class Path extends Shape {

    private List<Point> points;

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Path";
    }

}
