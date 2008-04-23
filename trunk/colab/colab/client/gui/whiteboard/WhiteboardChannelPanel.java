package colab.client.gui.whiteboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import colab.client.ClientWhiteboardChannel;
import colab.client.gui.ChannelPanelListener;
import colab.client.gui.ClientChannelPanel;
import colab.client.gui.FixedSizePanel;
import colab.client.gui.whiteboard.draw.DrawingTool;
import colab.common.DebugManager;
import colab.common.channel.document.DocumentParagraph;
import colab.common.channel.whiteboard.EditLayer;
import colab.common.channel.whiteboard.InsertLayer;
import colab.common.channel.whiteboard.WhiteboardChannelData;
import colab.common.channel.whiteboard.WhiteboardListener;
import colab.common.channel.whiteboard.draw.Figure;
import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.naming.UserName;

public class WhiteboardChannelPanel extends ClientChannelPanel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private final ClientWhiteboardChannel channel;

    private ToolPanel toolPanel;
    private DrawingPanel drawingPanel;
    private JColorChooser colorChooser;
    private LayerSelectionPanel layerPanel;

    private List<ChannelPanelListener> channelListeners;

    public WhiteboardChannelPanel(final UserName name,
            final ClientWhiteboardChannel channel) {

        super(name);

        this.channel = channel;

        this.channel.getWhiteboard().addWhiteboardListener(new WhiteboardListener() {

            public void onDelete(LayerIdentifier id) {
                // Nothing

            }

            public void onEdit(LayerIdentifier id) {
                drawingPanel.repaint();
            }

            public void onInsert(int offset, Layer layer) {
                // Nothing

            }

            public void onShift(LayerIdentifier id, int offset) {
                // Nothing

            }

        });

        drawingPanel = new DrawingPanel(this);
        drawingPanel.setPreferredSize(new Dimension(500, 400));

        channelListeners = new ArrayList<ChannelPanelListener>();

        toolPanel = new ToolPanel(this);
        toolPanel.setPreferredSize(new Dimension(100, 300));
        JPanel toolPanelWrapper = new FixedSizePanel(
                toolPanel, new Dimension(100, 200));

        colorChooser = new JColorChooser(Color.black);
        colorChooser.setPreferredSize(new Dimension(150, 150));
        colorChooser.setPreviewPanel(new JPanel());
        AbstractColorChooserPanel[] crap = colorChooser.getChooserPanels();
        colorChooser.removeChooserPanel(crap[1]);
        colorChooser.removeChooserPanel(crap[2]);

        layerPanel = new LayerSelectionPanel(this, channel.getWhiteboard());

        this.setLayout(new BorderLayout());
        add(toolPanelWrapper, BorderLayout.WEST);
        add(drawingPanel, BorderLayout.CENTER);
        add(colorChooser, BorderLayout.SOUTH);
        add(layerPanel, BorderLayout.EAST);

        this.setVisible(true);

    }

    /**
     * Gets the number of layers in this layer selection panel.
     * @return the number of layers
     */
    public int getNumberOfLayers() {
        return layerPanel.getNumberOfLayers();
    }

    /**
     * Redownloads document from client channel.
     */
    public void refreshLayerList() {
        layerPanel.refresh();
    }

    public void createNewLayer(final LayerIdentifier previous) {

        InsertLayer insert =
            new InsertLayer(super.getUsername(), new Date(), null);

        this.fireOnMessageSent(insert);

        layerPanel.repaint();
    }

    public void setTool(final DrawingTool tool) {
        drawingPanel.setTool(tool);
    }

    public void repaintCanvas() {
        drawingPanel.repaint();
    }

    public Color getDrawingColor() {
        return colorChooser.getColor();
    }

    public int getPenThickness() {
        return 2; // TODO Bug 89 - Changing pen thickness
    }

    public void addToActiveLayer(final Figure figure) {
        Layer activeLayer = layerPanel.getActiveLayer();


        activeLayer.addFigure(figure);

        // Send figure to server
        EditLayer edit = new EditLayer(super.getUsername(), new Date(),
                activeLayer.getId(),
                figure);

        this.fireOnMessageSent(edit);
    }

    public void drawLayers(final Graphics g) {
        layerPanel.drawLayers(g);
    }

    public void addChannelPanelListener(
            final ChannelPanelListener listener) {

        channelListeners.add(listener);

    }

    public void removeChannelPanelListener(
            final ChannelPanelListener listener) {

        channelListeners.remove(listener);

    }

    public void fireOnMessageSent(final WhiteboardChannelData wcd) {
        for (final ChannelPanelListener l : channelListeners) {
            l.onMessageSent(wcd);
        }
    }

//    public static void main(final String[] args) throws Exception {
//        JFrame frame = new JFrame("test");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(new WhiteboardChannelPanel(new UserName("UserName"),
//                new ClientWhiteboardChannel(new ChannelName("Channel name")) {
//            Whiteboard whiteboard;
//            public Whiteboard getWhiteboard() {
//                if (whiteboard == null) {
//                    whiteboard = new Whiteboard();
//                    Layer defaultLayer = new Layer(new LayerIdentifier(2));
//                    defaultLayer.setLabel("Default Label");
//                    whiteboard.insert(0, defaultLayer);
//                }
//                return whiteboard;
//            }
//        }));
//        frame.pack();
//        frame.setVisible(true);
//    }

}
