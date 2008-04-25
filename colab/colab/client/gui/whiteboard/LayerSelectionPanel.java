package colab.client.gui.whiteboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import colab.common.channel.whiteboard.Whiteboard;
import colab.common.channel.whiteboard.WhiteboardListener;
import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerIdentifier;

public class LayerSelectionPanel extends JPanel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private final JList panelList;

    private final Whiteboard whiteboard;

    private final Vector<LayerPanel> layerPanels;

    private final JButton newLayerButton;

    public LayerSelectionPanel(final WhiteboardChannelPanel panel,
            final Whiteboard whiteboard) {

        setLayout(new BorderLayout());

        add(new JLabel("Layers"), BorderLayout.NORTH);

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
            LayerPanel layerPanel = new LayerPanel(panelList, layer);
            layerPanels.add(layerPanel);
        }

        this.whiteboard = whiteboard;

        whiteboard.addWhiteboardListener(new WhiteboardListener() {
           public void onDelete(final LayerIdentifier id) {
                // TODO Bug 86 - Deleting layers

           }
           public void onInsert(final int offset, final Layer layer) {

               layerPanels.insertElementAt(
                       new LayerPanel(panelList, layer),
                       layerPanels.size() - offset);
               panelList.setListData(layerPanels);

           }

           public void onShift(final LayerIdentifier id, final int offset) {
            // TODO Bug 88 - Shifting layer order
           }

           public void onEdit(final LayerIdentifier id) {
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
                layerPanels.add(new LayerPanel(panelList, layers.next()));
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


    public void drawLayers(final Graphics g) {
        for (LayerPanel layerPanel : layerPanels) {
            layerPanel.getLayer().draw(g);
        }
    }

//    public static void main(final String[] args) {
//
//        Layer layer = new Layer(new LayerIdentifier(45));
//        layer.addFigure(new Ellipse(
//                new Point(0, 0),
//                new Dimension(100, 100),
//                Color.BLACK,
//                5,
//                false));
//        layer.setLabel("Layer title");
//
//        Layer layer2 = new Layer(new LayerIdentifier(4245));
//        layer2.addFigure(new Ellipse(
//                new Point(-50, -50),
//                new Dimension(100, 100),
//                Color.GREEN,
//                5,
//                false));
//        layer2.addFigure(new Ellipse(
//                new Point(50, 50),
//                new Dimension(100, 100),
//                Color.GREEN,
//                5,
//                false));
//        layer2.setLabel("ewgerge");
//
//        Whiteboard whiteboard = new Whiteboard();
//
//        LayerSelectionPanel panel = new LayerSelectionPanel(whiteboard);
//
//        whiteboard.insert(0, layer);
//        whiteboard.insert(0, layer2);
//        whiteboard.insert(0, layer);
//        whiteboard.insert(0, layer2);
//        whiteboard.insert(0, layer);
//        whiteboard.insert(0, layer2);
//        whiteboard.insert(0, layer);
//        whiteboard.insert(0, layer2);
//
//        JFrame frame = new JFrame();
//        frame.setPreferredSize(new Dimension(400, 600));
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setContentPane(panel);
//        frame.pack();
//        frame.setVisible(true);
//
//    }

}
