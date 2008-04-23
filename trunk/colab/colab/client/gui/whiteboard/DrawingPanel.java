package colab.client.gui.whiteboard;


import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import colab.client.gui.whiteboard.draw.DrawingTool;
import colab.common.channel.whiteboard.layer.Layer;

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

    /** {@inheritDoc} */
    @Override
    public void update(final Graphics g) {
        // Force repainting of the whole thing
        paintComponent(g);
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

        Layer activeLayer = parentPanel.getActiveLayer();
        for (Layer layer : parentPanel.getWhiteboard()) {
            layer.draw(g);
            if (tool != null && activeLayer == layer) {
                tool.draw(g);
            }
        }


    }

}
