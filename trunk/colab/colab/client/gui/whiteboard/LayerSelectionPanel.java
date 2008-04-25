package colab.client.gui.whiteboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import colab.client.ClientWhiteboardChannel;
import colab.common.channel.whiteboard.Whiteboard;
import colab.common.channel.whiteboard.WhiteboardListener;
import colab.common.channel.whiteboard.draw.Ellipse;
import colab.common.channel.whiteboard.draw.Figure;
import colab.common.channel.whiteboard.draw.Point;
import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.channel.whiteboard.layer.LayerListener;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;

public class LayerSelectionPanel extends JPanel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private final JList panelList;

    private final Whiteboard whiteboard;

    private final WhiteboardChannelPanel panel;

    private final Vector<LayerPanel> layerPanels;

    private List<LayerSelectionPanelListener> listeners;
    
    private Layer lockedLayer = null;
    
    private final JButton newLayerButton;

    public LayerSelectionPanel(final WhiteboardChannelPanel panel,
            final Whiteboard whiteboard) {

        setLayout(new BorderLayout());

        add(new JLabel("Layers"), BorderLayout.NORTH);

        this.panel = panel;
        listeners = new ArrayList<LayerSelectionPanelListener>();
        
        layerPanels = new Vector<LayerPanel>();

        panelList = new JList(layerPanels);
        panelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelList.setCellRenderer(new ListCellRenderer() {
            public Component getListCellRendererComponent(final JList list,
                    final Object value, final int index,
                    final boolean isSelected, final boolean cellHasFocus) {
                LayerPanel layerPanel = (LayerPanel) value;
                layerPanel.setSelected(isSelected);
                return layerPanel;
            }
        });
        panelList.setBackground(Color.LIGHT_GRAY);

        add(new JScrollPane(panelList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        
        for (Layer layer : whiteboard) {
            LayerPanel layerPanel = new LayerPanel(panelList, layer, panel.getUsername());
            layer.addLayerListener(new LayerListener() {
            	public void onLabelChange(final String newLabel) {
                }
                public void onFigureAdded(final Figure figure) {
                }
                public void onLockChange(UserName lockHolder) {
                	if(lockHolder == null &&
                			lockedLayer!= null &&
                			layer.getId().equals(lockedLayer.getId())) {
                		lockedLayer = null;
                	}
                	else if(lockHolder != null && lockHolder.equals(panel.getUsername())) {
                        lockedLayer = layer;
                    }
                }
            });
            layerPanels.add(layerPanel);
        }

        this.whiteboard = whiteboard;

        whiteboard.addWhiteboardListener(new WhiteboardListener() {
           public void onDelete(final LayerIdentifier id) {
                // TODO Bug 86 - Deleting layers

           }
           public void onInsert(final int offset, final Layer layer) {

               layerPanels.insertElementAt(
                       new LayerPanel(panelList, layer, panel.getUsername()),
                       layerPanels.size() - offset);
               panelList.setListData(layerPanels);

           }

           public void onShift(final LayerIdentifier id, final int offset) {
            // TODO Bug 88 - Shifting layer order
           }

           public void onEdit(final LayerIdentifier id) {
           }
        });

        panelList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e) {
        		
        		if(lockedLayer != null) {
    				fireOnLockRequest(lockedLayer, null);
    			}
        		
        		if(!e.getValueIsAdjusting()) {
        			int index = panelList.getSelectionModel().getMinSelectionIndex();
        			Layer l = layerPanels.elementAt(index).getLayer();
        			
        			fireOnLockRequest(l, panel.getUsername());
        		}
        	}
        });
        
        newLayerButton = new JButton("New Layer");
        newLayerButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent arg0) {
                Layer activeLayer = getActiveLayer();
                LayerIdentifier previousLayer;
                if (activeLayer == null) {
                    previousLayer = null;
                } else {
                    previousLayer = activeLayer.getId();
                }
                panel.createNewLayer(previousLayer);
            }

        });

        add(newLayerButton, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(175, 123));

        
    }

    public void refresh() {
        if (whiteboard != null) {

            layerPanels.clear();
            Iterator<Layer> layers = whiteboard.iterator();

            while (layers.hasNext()) {
                // Insert newer layers at the front
                layerPanels.add(0, new LayerPanel(panelList, layers.next(), panel.getUsername()));
            }
        }
        panelList.setListData(layerPanels);
    }

    public Layer getActiveLayer() {

        LayerPanel layerPanel = (LayerPanel) panelList.getSelectedValue();

        if (layerPanel == null) {
            return null;
        }

        return layerPanel.getLayer();

    }

    /**
     * Gets the number of layers in this layer selection panel.
     * @return the number of layers
     */
    public int getNumberOfLayers() {
        return layerPanels.size();
    }

    public Layer getLockedLayer() {
    	return lockedLayer;
    }
    
    public void drawLayers(final Graphics g) {
        for (LayerPanel layerPanel : layerPanels) {
            layerPanel.getLayer().draw(g);
        }
    }

    public void addLayerSelectionPanelListener(LayerSelectionPanelListener listener) {
    	listeners.add(listener);
    }
    
    protected void fireOnLockRequest(Layer requested, UserName requester) {
    	for(LayerSelectionPanelListener l : listeners) {
    		l.onLockRequest(requested, requester);
    	}
    }
    
    public static void main(final String[] args) throws Exception{
        Layer layer = new Layer(new LayerIdentifier(45));
        layer.addFigure(new Ellipse(
                new Point(0, 0),
                new Dimension(100, 100),
                Color.BLACK,
                5,
                false));
        layer.setLabel("Layer title");

        Layer layer2 = new Layer(new LayerIdentifier(4245));
        layer2.addFigure(new Ellipse(
                new Point(-50, -50),
                new Dimension(100, 100),
                Color.GREEN,
                5,
                false));
        layer2.addFigure(new Ellipse(
                new Point(50, 50),
                new Dimension(100, 100),
                Color.GREEN,
                5,
                false));
        layer2.setLabel("ewgerge");

        Whiteboard whiteboard = new Whiteboard();

        LayerSelectionPanel panel = new LayerSelectionPanel(new WhiteboardChannelPanel(new UserName("Matt"), new ClientWhiteboardChannel(new ChannelName("Jolly"))), whiteboard);

        whiteboard.insert(0, layer);
        whiteboard.insert(0, layer2);
        whiteboard.insert(0, layer);
        whiteboard.insert(0, layer2);
        whiteboard.insert(0, layer);
        whiteboard.insert(0, layer2);
        whiteboard.insert(0, layer);
        whiteboard.insert(0, layer2);
        layer2.lock(new UserName("Steve"));
        layer.lock(new UserName("Matt"));
        layer.unlock();
        
        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(400, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);

    }

}
