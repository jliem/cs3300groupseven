package colab.client.gui.whiteboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import colab.common.channel.whiteboard.Whiteboard;
import colab.common.channel.whiteboard.WhiteboardListener;
import colab.common.channel.whiteboard.draw.Ellipse;
import colab.common.channel.whiteboard.draw.Point;
import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerIdentifier;

public class LayerSelectionPanel extends JPanel {

    public static final long serialVersionUID = 1;

    private final JList panelList;

    private final Whiteboard whiteboard;

    private final Vector<LayerPanel> layerPanels;

    public LayerSelectionPanel(final Whiteboard whiteboard) {

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
            LayerPanel layerPanel = new LayerPanel(layer);
            layerPanels.add(layerPanel);
        }

        this.whiteboard = whiteboard;

        whiteboard.addWhiteboardListener(new WhiteboardListener() {
           public void onDelete(final LayerIdentifier id) {
                // TODO Auto-generated method stub

            }
           public void onInsert(final int offset, final Layer layer) {
                layerPanels.insertElementAt(new LayerPanel(layer), offset);
           }
           public void onShift(final LayerIdentifier id, final int offset) {
            // TODO Auto-generated method stub

           }
        });

        setPreferredSize(new Dimension(175, 123));

    }

    public static void main(final String[] args) {

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

        LayerSelectionPanel panel = new LayerSelectionPanel(whiteboard);

        whiteboard.insert(0, layer);
        whiteboard.insert(0, layer2);
        whiteboard.insert(0, layer);
        whiteboard.insert(0, layer2);
        whiteboard.insert(0, layer);
        whiteboard.insert(0, layer2);
        whiteboard.insert(0, layer);
        whiteboard.insert(0, layer2);

        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(400, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);

    }

}
