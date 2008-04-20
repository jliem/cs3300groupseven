package colab.client.gui.whiteboard;


import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import colab.client.gui.whiteboard.draw.DrawingTool;

/**
 * Drawing panel for whiteboard.
 */
public class DrawingPanel extends JPanel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private WhiteboardChannelPanel parentPanel;

    private DrawingTool tool;

    public DrawingPanel(final WhiteboardChannelPanel parentPanel) {

        this.parentPanel = parentPanel;

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

        parentPanel.drawLayers(g);

        if (tool != null) {
            tool.draw(g);
        }

    }

}
