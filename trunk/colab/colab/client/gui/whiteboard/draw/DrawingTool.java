package colab.client.gui.whiteboard.draw;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import colab.client.gui.whiteboard.WhiteboardChannelPanel;
import colab.common.channel.whiteboard.draw.Figure;

/**
 * A drawing tool is a construct which uses mouse events
 * to construct Figures on a Layer.
 */
public abstract class DrawingTool
        implements MouseListener, MouseMotionListener {

    private final WhiteboardChannelPanel parentPanel;

    private Point start;

    /**
     * Constucts a new DrawingTool.
     *
     * @param parentPanel the panel which created this tool
     */
    public DrawingTool(final WhiteboardChannelPanel parentPanel) {
        this.parentPanel = parentPanel;
    }

    /**
     * Sends a completed drawn figure to the parent panel.
     *
     * @param figure the figure to draw
     */
    protected final void drawFigure(final Figure figure) {
        parentPanel.addToActiveLayer(figure);
    }

    /**
     * Instructs the parent panel to repaint itself.
     */
    protected final void repaint() {
        parentPanel.repaintCanvas();
    }

    /**
     * @return the point at which the mouse down event was registered
     */
    protected final Point getStart() {
        return new Point(this.start);
    }

    /**
     * @return the panel which created this tool
     */
    protected final WhiteboardChannelPanel getParentPanel() {
        return this.parentPanel;
    }

    /**
     * Draws the tool.
     *
     * @param g the graphics object to draw on
     */
    public void draw(final Graphics g) {
    }

    /**
     * Sets the start position.
     *
     * {@inheritDoc}
     */
    public void mousePressed(final MouseEvent e) {
        this.start = e.getPoint();
    }

    /** {@inheritDoc} */
    public void mouseDragged(final MouseEvent e) {
    }

    /** {@inheritDoc} */
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
