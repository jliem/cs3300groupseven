package colab.client.gui.whiteboard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import colab.common.channel.whiteboard.draw.Figure;
import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerListener;
import colab.common.util.ImageUtils;

public class LayerPanel extends JPanel {

    private final Layer layer;

    private ImageIcon preview;

    private final JTextField label;

    private static final Dimension PREVIEW_SIZE = new Dimension(50, 50);

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

        setLayout(new BorderLayout());

        preview = new ImageIcon();
        JButton previewButton = new JButton(preview);
        previewButton.setPreferredSize(PREVIEW_SIZE);

        add(BorderLayout.WEST, new JButton(preview));
        add(BorderLayout.CENTER, label);

        refreshThumb();

    }

    private void setLabelText(final String label) {
        this.label.setText(label);
    }

    private void refreshThumb() {
        BufferedImage image = layer.croppedImage();
        Image scaledImage = ImageUtils.scaleToFit(
                image, PREVIEW_SIZE, this);

        preview.setImage(scaledImage);
    }

}
