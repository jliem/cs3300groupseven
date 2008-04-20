package colab.client.gui.whiteboard;


import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import colab.client.gui.whiteboard.draw.DrawingTool;
import colab.common.channel.whiteboard.draw.Figure;

/**
 * Drawing panel for whiteboard.
 */
public class DrawingPanel extends JPanel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private DrawingTool tool;

    public DrawingPanel() {
        setBackground(Color.WHITE);
    }

    public void setTool(final DrawingTool newTool) {

        if (tool != null) {
            this.removeMouseListener(tool);
            this.removeMouseMotionListener(tool);
        }

        this.addMouseListener(newTool);
        this.addMouseMotionListener(newTool);
        this.tool = newTool;

    }

    public void paintComponent(final Graphics g) {

        super.paintComponent(g);

        if (tool != null) {
            tool.draw(g);
        }

    }

    public Color getColor() {
        return Color.BLUE; // TODO get from the color picker
    }

    public int getPenThickness() {
        return 2; // TODO make a ui component for this
    }

    public void drawFigure(final Figure figure) {
        // TODO add the figure to the active layer
    }

}
