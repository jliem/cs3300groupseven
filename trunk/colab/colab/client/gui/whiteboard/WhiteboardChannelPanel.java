package colab.client.gui.whiteboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.rmi.RemoteException;
import java.util.Date;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import colab.client.ClientWhiteboardChannel;
import colab.client.gui.ClientChannelPanel;
import colab.client.gui.FixedSizePanel;
import colab.client.gui.whiteboard.draw.DrawingTool;
import colab.common.DebugManager;
import colab.common.channel.whiteboard.InsertLayer;
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

    public WhiteboardChannelPanel(final UserName name,
            final ClientWhiteboardChannel channel) {

        super(name);

        this.channel = channel;

        drawingPanel = new DrawingPanel(this);
        drawingPanel.setPreferredSize(new Dimension(500, 400));

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

        // TODO Download existing data

        if (layerPanel.getNumberOfLayers() <= 0) {
            this.createNewLayer(null);
        }

        this.setVisible(true);

    }

    public void createNewLayer(final LayerIdentifier previous) {
        InsertLayer insert =
            new InsertLayer(super.getUsername(), new Date(),
                    null, new Layer(null));


        // TODO Fix me
        try {
            channel.add(insert);
        } catch (RemoteException re) {
            DebugManager.remote(re);
        }


        //this.fireOnMessageSent(data);


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
        return 2; // TODO make a ui component for this
    }

    public void addToActiveLayer(final Figure figure) {
        layerPanel.getActiveLayer().addFigure(figure);
    }

    public void drawLayers(final Graphics g) {
        layerPanel.drawLayers(g);
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
