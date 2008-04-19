package colab.client.gui.whiteboard;

import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.JTextField;

import colab.common.channel.whiteboard.draw.Figure;
import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerListener;

public class LayerPanel extends JPanel {

    private final Layer layer;

    private Image preview;

    private final JTextField label;

    public LayerPanel(final Layer layer) {

        this.layer = layer;
        this.label = new JTextField();

        layer.addLayerListener(new LayerListener() {
            public void onLabelChange(final String newLabel) {
                setLabelText(newLabel);
            }
            public void onFigureAdded(final Figure figure) {
                refreshThumb();
            }

        });

        setLabelText(layer.getLabel());

    }

    private void setLabelText(final String label) {
        this.label.setText(label);
    }

    private void refreshThumb() {
        // TODO
    }

}
