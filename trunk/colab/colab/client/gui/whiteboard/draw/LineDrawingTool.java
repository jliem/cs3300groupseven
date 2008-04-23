package colab.client.gui.whiteboard.draw;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import colab.client.gui.whiteboard.WhiteboardChannelPanel;
import colab.common.channel.whiteboard.draw.Figure;
import colab.common.channel.whiteboard.draw.Path;

public class LineDrawingTool extends DrawingTool {

    private Path line;

    public LineDrawingTool(final WhiteboardChannelPanel parentPanel) {
        super(parentPanel);
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
                getParentPanel().getDrawingColor(),
                getParentPanel().getPenThickness());
        getParentPanel().repaintCanvas();
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(final MouseEvent e) {
        super.mouseReleased(e);
        Figure toDraw = line;
        line = null;
        drawFigure(toDraw);
    }

    /** {@inheritDoc} */
    @Override
    public void draw(final Graphics g) {
        super.draw(g);
        if (line != null) {
            line.draw(g);
        }
    }

}
