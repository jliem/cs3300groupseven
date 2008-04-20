package colab.client.gui.whiteboard.draw;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import colab.client.gui.whiteboard.DrawingPanel;
import colab.common.channel.whiteboard.draw.Figure;

public abstract class DrawingTool
        implements MouseListener, MouseMotionListener {

    private DrawingPanel canvas;

    private Point start;

    public DrawingTool(final DrawingPanel canvas) {

        this.canvas = canvas;

    }

    protected final void drawFigure(final Figure figure) {
        //canvas.drawFigure(figure);
    }

    protected final void repaint() {
        canvas.repaint();
    }

    protected final Point getStart() {
        return new Point(this.start);
    }

    protected final DrawingPanel getCanvas() {
        return this.canvas;
    }

    public void draw(final Graphics g) {
    }

    public void mousePressed(final MouseEvent e) {
        this.start = e.getPoint();
    }

    public void mouseDragged(final MouseEvent e) {
    }

    public void mouseReleased(final MouseEvent e) {
    }

    /** {@inheritDoc} */
    public final void mouseClicked(final MouseEvent e) {
    }

    /** {@inheritDoc} */
    public final void mouseEntered(final MouseEvent e) {
    }

    /** {@inheritDoc} */
    public final void mouseExited(final MouseEvent e) {
    }

    /** {@inheritDoc} */
    public final void mouseMoved(final MouseEvent e) {
    }

}
