package colab.client.gui.whiteboard.draw;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import colab.client.gui.whiteboard.WhiteboardChannelPanel;
import colab.common.channel.whiteboard.draw.Ellipse;
import colab.common.channel.whiteboard.draw.Point;
import colab.common.channel.whiteboard.draw.Rectangle;

public class EllipseDrawingTool extends DrawingTool {

    private Ellipse ellipse;

    private boolean filled;

    public EllipseDrawingTool(final WhiteboardChannelPanel parentPanel,
            final boolean filled) {
        super(parentPanel);
        this.filled = filled;
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(final MouseEvent e) {
        super.mousePressed(e);
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(final MouseEvent e) {
        super.mouseDragged(e);
        Point position = new Point(
                Math.min(getStart().x, e.getPoint().x),
                Math.min(getStart().y, e.getPoint().y));
        Dimension size = new Dimension(
                Math.abs(getStart().x - e.getPoint().x),
                Math.abs(getStart().y - e.getPoint().y));
        ellipse = new Ellipse(position, size,
                getParentPanel().getDrawingColor(),
                getParentPanel().getPenThickness(), filled);
        getParentPanel().repaintCanvas();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(final MouseEvent e) {
        super.mouseReleased(e);
        drawFigure(ellipse);
    }

    /** {@inheritDoc} */
    @Override
    public void draw(final Graphics g) {
        super.draw(g);
        ellipse.draw(g);
    }

}
