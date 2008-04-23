package colab.client.gui.whiteboard.draw;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import colab.client.gui.whiteboard.WhiteboardChannelPanel;
import colab.common.channel.whiteboard.draw.Ellipse;
import colab.common.channel.whiteboard.draw.Figure;
import colab.common.channel.whiteboard.draw.Point;

public class EllipseDrawingTool extends DrawingTool {

    private Ellipse ellipse;

    private boolean filled;

    /**
     * Constructs a new EllipseDrawingTool.
     *
     * @param parentPanel the panel to draw on
     * @param filled whether the ellipse is filled with color
     */
    public EllipseDrawingTool(
            final WhiteboardChannelPanel parentPanel,
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
        Figure toDraw = ellipse;
        ellipse = null;
        drawFigure(toDraw);
    }

    /** {@inheritDoc} */
    @Override
    public void draw(final Graphics g) {
        super.draw(g);
        if (ellipse != null) {
            ellipse.draw(g);
        }
    }

}
