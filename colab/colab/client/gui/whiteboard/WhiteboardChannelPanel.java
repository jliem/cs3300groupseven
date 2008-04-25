package colab.client.gui.whiteboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import colab.client.ClientWhiteboardChannel;
import colab.client.gui.ChannelPanelListener;
import colab.client.gui.ClientChannelPanel;
import colab.client.gui.FixedSizePanel;
import colab.client.gui.whiteboard.draw.DrawingTool;
import colab.common.channel.whiteboard.DeleteLayer;
import colab.common.channel.whiteboard.EditLayer;
import colab.common.channel.whiteboard.InsertLayer;
import colab.common.channel.whiteboard.LockLayer;
import colab.common.channel.whiteboard.Whiteboard;
import colab.common.channel.whiteboard.WhiteboardChannelData;
import colab.common.channel.whiteboard.WhiteboardListenerAdapter;
import colab.common.channel.whiteboard.draw.Figure;
import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;

public class WhiteboardChannelPanel extends ClientChannelPanel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private final ClientWhiteboardChannel channel;

    private final Whiteboard whiteboard;

    private final ToolPanel toolPanel;
    private final DrawingPanel drawingPanel;
    private final JColorChooser colorChooser;
    private final LayerSelectionPanel layerPanel;
    
    private final Timer lockTimer;

    private final List<ChannelPanelListener> channelListeners;

    private static final int UNLOCK_DELAY = 15; /*delay to automatically yield lock, in seconds*/
    
    public WhiteboardChannelPanel(final UserName name,
            final ClientWhiteboardChannel channel) {

        super(name);

        this.channel = channel;
        
        this.whiteboard = this.channel.getWhiteboard();
        whiteboard.addWhiteboardListener(
                new WhiteboardListenerAdapter() {
            public void onEdit(final LayerIdentifier id) {
                drawingPanel.repaint();
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
        layerPanel.addLayerSelectionPanelListener(new LayerSelectionPanelListener() {
        	public void onLockRequest(Layer requested, UserName requester) {
        		LockLayer lockReq = new LockLayer(name, new Date(), requested.getId(), requester);
        		fireOnMessageSent(lockReq);
        	}
        	public void onDelete(LayerIdentifier id) {
        		DeleteLayer delete = new DeleteLayer(name, new Date(), id);
        		fireOnMessageSent(delete);
        	}
        });

        lockTimer = new Timer(1000 * UNLOCK_DELAY, new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(layerPanel.getLockedLayer() != null) {
	        		LockLayer lockReq = new LockLayer(name, new Date(), layerPanel.getLockedLayer().getId(), null);
	        		fireOnMessageSent(lockReq);
        		}
        	}
        });
        
        this.setLayout(new BorderLayout());
        add(toolPanelWrapper, BorderLayout.WEST);
        add(drawingPanel, BorderLayout.CENTER);
        add(colorChooser, BorderLayout.SOUTH);
        add(layerPanel, BorderLayout.EAST);

        this.setVisible(true);
        
        lockTimer.start();
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
            new InsertLayer(super.getUsername(), new Date(), previous);

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

    public Layer getActiveLayer() {
        return layerPanel.getActiveLayer();
    }

    public void addToActiveLayer(final Figure figure) {

        Layer activeLayer = getActiveLayer();

        if (activeLayer == null) {
            return;
        }

        activeLayer.addFigure(figure);

        // Send figure to server
        EditLayer edit = new EditLayer(super.getUsername(), new Date(),
                activeLayer.getId(),
                figure);

        this.fireOnMessageSent(edit);

        this.drawingPanel.repaint();

    }

    public Whiteboard getWhiteboard() {
        return whiteboard;
    }
    
    public ClientWhiteboardChannel getChannel() {
        return channel;
    }

    public void addChannelPanelListener(
            final ChannelPanelListener listener) {

        channelListeners.add(listener);

    }

    public void removeChannelPanelListener(
            final ChannelPanelListener listener) {

        channelListeners.remove(listener);

    }

    protected void fireOnMessageSent(final WhiteboardChannelData wcd) {
        for (final ChannelPanelListener l : channelListeners) {
            l.onMessageSent(wcd);
        }
    }

    public void retainLock() {
    	lockTimer.restart();
    }
    public static void main(final String[] args) throws Exception {
        JFrame frame = new JFrame("test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new WhiteboardChannelPanel(new UserName("UserName"),
                new ClientWhiteboardChannel(new ChannelName("Channel name")) {
            Whiteboard whiteboard;
            public Whiteboard getWhiteboard() {
                if (whiteboard == null) {
                    whiteboard = new Whiteboard();
                    Layer defaultLayer = new Layer(new LayerIdentifier(2));
                    defaultLayer.setLabel("Default Label");
                    whiteboard.insert(0, defaultLayer);
                }
                return whiteboard;
            }
        }));
        frame.pack();
        frame.setVisible(true);
    }

}
