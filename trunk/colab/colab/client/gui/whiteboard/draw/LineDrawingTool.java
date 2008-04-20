package colab.client.gui.whiteboard.draw;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import colab.client.gui.whiteboard.DrawingPanel;
import colab.common.channel.whiteboard.draw.Path;

public class LineDrawingTool extends DrawingTool {

    private Path line;

    public LineDrawingTool(final DrawingPanel canvas) {
        super(canvas);
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
        line = new Path(getStart(), e.getPoint(),
                getCanvas().getColor(), getCanvas().getPenThickness());
        getCanvas().repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(final MouseEvent e) {
        super.mouseReleased(e);
        //drawFigure();
    }

    /** {@inheritDoc} */
    @Override
    public void draw(final Graphics g) {
        super.draw(g);
        line.draw(g);
    }

}
