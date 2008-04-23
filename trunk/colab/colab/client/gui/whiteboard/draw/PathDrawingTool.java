package colab.client.gui.whiteboard.draw;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import colab.client.gui.whiteboard.WhiteboardChannelPanel;
import colab.common.channel.whiteboard.draw.Figure;
import colab.common.channel.whiteboard.draw.Path;
import colab.common.channel.whiteboard.draw.Point;

public class PathDrawingTool extends DrawingTool {

    private Path path;

    public PathDrawingTool(final WhiteboardChannelPanel parentPanel) {
        super(parentPanel);
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(final MouseEvent e) {
        super.mousePressed(e);
        path = new Path(new Point(0, 0),
                getParentPanel().getDrawingColor(),
                getParentPanel().getPenThickness());
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(final MouseEvent e) {
        super.mouseDragged(e);
        path.addPoint(new Point(e.getPoint()));
        getParentPanel().repaintCanvas();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(final MouseEvent e) {
        super.mouseReleased(e);
        Figure toDraw = path;
        path = null;
        drawFigure(toDraw);
    }

    /** {@inheritDoc} */
    @Override
    public void draw(final Graphics g) {
        super.draw(g);
        if (path != null) {
            path.draw(g);
        }
    }

}
