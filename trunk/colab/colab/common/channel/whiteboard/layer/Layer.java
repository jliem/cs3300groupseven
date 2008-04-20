package colab.common.channel.whiteboard.layer;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import colab.common.channel.whiteboard.Drawable;
import colab.common.channel.whiteboard.draw.Figure;
import colab.common.identity.Identifiable;

/**
 * A layer is a 2-dimensional image.  Multiple layers can be
 * overlayed upon each other within a whiteboard channel.
 */
public class Layer implements Identifiable<LayerIdentifier>, Drawable {

    private LayerIdentifier id;

    /**
     * The user-specified label for this layer;
     * doesn't have to be unique.
     */
    private String label;

    private final List<LayerListener> listeners;

    private final List<Figure> figures;

    /** Derived from the figures list. */
    private final java.awt.Rectangle contentBounds;

    /**
     * Constructs a new Layer.
     *
     * @param id the layer identifier
     */
    public Layer(final LayerIdentifier id) {
        this.id = id;
        this.listeners = new ArrayList<LayerListener>();
        this.figures = new LinkedList<Figure>();
        this.contentBounds = new java.awt.Rectangle(0, 0, 0, 0);
    }

    public void addLayerListener(final LayerListener listener) {
        this.listeners.add(listener);
    }

    public void removeLayerListener(final LayerListener listener) {
        this.listeners.remove(listener);
    }

    protected void fireOnLabelChange(final String newLabel) {
        for (LayerListener listener : listeners) {
            listener.onLabelChange(newLabel);
        }
    }

    protected void fireOnFigureAdded(final Figure figure) {
        for (LayerListener listener : listeners) {
            listener.onFigureAdded(figure);
        }
    }

    /** {@inheritDoc} */
    public LayerIdentifier getId() {
        return id;
    }

    /** {@inheritDoc} */
    public void setId(final LayerIdentifier id) {
        this.id = id;
    }

    /**
     * @return the label for this layer
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label for this layer
     */
    public void setLabel(final String label) {
        this.label = label;
        this.fireOnLabelChange(label);
    }

    public void addFigure(final Figure figure) {

        // Add the figure to the list
        this.figures.add(figure);

        // Expands the bounds if necessary to contain the new figure
        this.contentBounds.add(figure.getBounds());

        // Notify listeners
        this.fireOnFigureAdded(figure);

    }

    public void draw(final Graphics graphIn) {
        for (Figure figure : figures) {
            figure.draw(graphIn);
        }
    }

    public BufferedImage croppedImage() {

        BufferedImage image = new BufferedImage(
                contentBounds.width, contentBounds.height,
                BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();

        g.fillRect(0, 0, contentBounds.width, contentBounds.height);

        g.translate(-contentBounds.x, -contentBounds.y);

        draw(g);

        g.translate(contentBounds.x, contentBounds.y);

        return image;

    }

}
