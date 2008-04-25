package colab.client.gui.whiteboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import colab.common.DebugManager;
import colab.common.channel.whiteboard.draw.Figure;
import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerListener;
import colab.common.naming.UserName;
import colab.common.util.ImageUtils;

/**
 * A small panel reprenting a whiteboard layer.
 *
 * It shows a thumbnail, and the layer name.
 */
public class LayerPanel extends JPanel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private final Layer layer;

    private ImageIcon preview, status;

    private Image locked, active;
    
    private final JLabel label;

    private final JList list;

    private final UserName user;
    
    private static final Dimension PREVIEW_SIZE = new Dimension(50, 50), STATUS_SIZE = new Dimension(50, 50);

    private JButton previewButton, statusButton;

    /**
     * Constructs a new LayerPanel.
     *
     * @param list the list which contains this panel
     * @param layer the layer this panel represents
     */
    public LayerPanel(final JList list,
            final Layer layer, UserName thisUser) {

        this.list = list;
        this.layer = layer;
        this.label = new JLabel();

        user = thisUser;
        
        layer.addLayerListener(new LayerListener() {
            public void onLabelChange(final String newLabel) {
                setLabelText(newLabel);
            }
            public void onFigureAdded(final Figure figure) {
                refreshThumb();
            }
            public void onLockChange(UserName lockHolder) {
                if(lockHolder == null) {
                    status.setImage(new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB));
                }
                else if(lockHolder.equals(user)) {
                    status.setImage(active);
                } else {
                    status.setImage(locked);
                }
            }
        });

        setLabelText(layer.getLabel());

        setLayout(new BorderLayout());

        try {
            locked = ImageIO.read(new File("whiteboard_layer_lock.png"));
        }
        catch(IOException ex) {
            DebugManager.exception(ex);
            locked = new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
        }
        
        try {
            active = ImageIO.read(new File("whiteboard_layer_active.png"));
        }
        catch(IOException ex) {
            DebugManager.exception(ex);
            active = new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
        }
        
        preview = new ImageIcon();
        previewButton = new JButton(preview);
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

        status = new ImageIcon();
        statusButton = new JButton(status);
        statusButton.setMaximumSize(STATUS_SIZE);
        statusButton.setOpaque(false);
        JPanel statusPanel = new JPanel();
        statusPanel.add(statusButton);
        statusPanel.setPreferredSize(
                new Dimension(STATUS_SIZE.width + 20,
                        STATUS_SIZE.height + 20));
        statusPanel.setBackground(Color.LIGHT_GRAY);

        statusButton.setBackground(Color.LIGHT_GRAY);
        statusButton.setBorderPainted(false);
        
        add(BorderLayout.WEST, previewPanel);
        add(BorderLayout.CENTER, label);
        add(BorderLayout.EAST, statusPanel);
        
        refreshThumb();

    }

    private void setLabelText(final String label) {
        this.label.setText("  " + label);
    }

    /**
     * @param selected whether the panel is currently selected
     */
    public void setSelected(final boolean selected) {
        if (selected) {
            setBackground(Color.CYAN);
        } else {
            setBackground(Color.LIGHT_GRAY);
        }
    }

    private void refreshThumb() {
        BufferedImage image = layer.croppedImage();
        if (image != null) {
            Image scaledImage = ImageUtils.scaleToFit(
                    image, PREVIEW_SIZE, this);
            previewButton.setIcon(new ImageIcon(scaledImage));

        }

        list.repaint();
    }

    /**
     * @return the layer represented by this panel
     */
    public Layer getLayer() {
        return layer;
    }

}
