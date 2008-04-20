package colab.client.gui.whiteboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import colab.common.channel.whiteboard.draw.Ellipse;
import colab.common.channel.whiteboard.draw.Figure;
import colab.common.channel.whiteboard.draw.Point;
import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.channel.whiteboard.layer.LayerListener;
import colab.common.util.ImageUtils;

public class LayerPanel extends JPanel {

    private final Layer layer;

    private ImageIcon preview;

    private final JLabel label;

    private static final Dimension PREVIEW_SIZE = new Dimension(50, 50);

    public LayerPanel(final Layer layer) {

        this.layer = layer;
        this.label = new JLabel();

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
        previewButton.setMaximumSize(PREVIEW_SIZE);
        previewButton.setOpaque(false);
        JPanel previewPanel = new JPanel();
        previewPanel.add(previewButton);
        previewPanel.setPreferredSize(
                new Dimension(PREVIEW_SIZE.width + 20,
                        PREVIEW_SIZE.height + 20));
        previewPanel.setBackground(Color.LIGHT_GRAY);

        previewButton.setBackground(Color.LIGHT_GRAY);
        previewButton.setBorderPainted(false);

        add(BorderLayout.WEST, previewPanel);
        add(BorderLayout.CENTER, label);

        refreshThumb();

    }

    private void setLabelText(final String label) {
        this.label.setText(label);
    }

    public void setSelected(boolean selected) {
        if(selected) {
            setBackground(Color.BLUE.brighter());
        } else {
            setBackground(Color.LIGHT_GRAY);
        }
    }

    private void refreshThumb() {
        System.out.println("Refreshing thumb");
        BufferedImage image = layer.croppedImage();
        if (image != null) {
            System.out.println("Image is displaying");
            Image scaledImage = ImageUtils.scaleToFit(
                    image, PREVIEW_SIZE, this);
            preview.setImage(scaledImage);
        }
    }

    public Layer getLayer() {
        return layer;
    }

    public static void main(final String[] args) {

        Layer layer = new Layer(new LayerIdentifier(45));
        layer.addFigure(new Ellipse(
                new Point(0, 0),
                new Dimension(100, 100),
                Color.BLACK,
                5,
                false));

        LayerPanel panel = new LayerPanel(layer);

        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(400, 100));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);


    }

}
