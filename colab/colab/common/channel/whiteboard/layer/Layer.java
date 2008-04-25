package colab.common.channel.whiteboard.layer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import colab.common.channel.whiteboard.Drawable;
import colab.common.channel.whiteboard.draw.Figure;
import colab.common.identity.Identifiable;
import colab.common.naming.UserName;

/**
 * A layer is a 2-dimensional image.  Multiple layers can be
 * overlayed upon each other within a whiteboard channel.
 */
public class Layer
        implements Identifiable<LayerIdentifier>, Drawable, Serializable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private LayerIdentifier id;

    /**
     * The user-specified label for this layer;
     * doesn't have to be unique.
     */
    private String label;

    private final List<LayerListener> listeners;

    private final List<Figure> figures;

    private UserName lockHolder = null;

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

        if (id != null) {
            this.label = "Layer " + id.getValue();
        } else {
            this.label = "Layer";
        }
    }

    /**
     * @param listener a listener to add
     */
    public void addLayerListener(final LayerListener listener) {
        this.listeners.add(listener);
    }

    /**
     * @param listener a listener to remove
     */
    public void removeLayerListener(final LayerListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * Informs listeners that the layer's name has changed.
     *
     * @param newLabel the new name
     */
    protected void fireOnLabelChange(final String newLabel) {
        for (LayerListener listener : listeners) {
            listener.onLabelChange(newLabel);
        }
    }

    /**
     * Informs listeners that a figure has been added to this layer.
     *
     * @param figure the new figure
     */
    protected void fireOnFigureAdded(final Figure figure) {
        for (LayerListener listener : listeners) {
            listener.onFigureAdded(figure);
        }
    }

    /**
     * Sets the lock holder.
     *
     * @param user the name of the user with a lock
     */
    public void lock(final UserName user) {
       if (isUnlocked()) {
           lockHolder = user;
       }
    }

    /**
     * Clears the lock holder.
     */
    public void unlock() {
        lockHolder = null;
    }

    /**
     * @return false if the layer is locked, true otherwise
     */
    public boolean isUnlocked() {
        return lockHolder == null;
    }

    /**
     * @return the name of the user who holds a lock on this layer
     */
    public UserName getLockHolder() {
        return lockHolder;
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
     * @return the minimum rectangle that contains every stroke in this layer
     */
    public java.awt.Rectangle getBounds() {
        return contentBounds;
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

    /**
     * Removes all figures from this Layer.
     */
    public void clear() {
        figures.clear();
    }

    /**
     * @return a deep copy of this layer
     */
    public Layer copy() {
        Layer layer = new Layer(new LayerIdentifier(getId()));

        for (Figure figure : figures) {
            layer.addFigure(figure.copy());
        }

        return layer;
    }

    /**
     * Adds a figure to this layer.
     *
     * @param figure the figure to add
     */
    public void addFigure(final Figure figure) {

        // Add the figure to the list
        this.figures.add(figure);

        // Expands the bounds if necessary to contain the new figure
        if (this.contentBounds.height * this.contentBounds.width == 0) {
            this.contentBounds.setBounds(figure.getBounds());
        } else {
            this.contentBounds.add(figure.getBounds());
        }

        // Notify listeners
        this.fireOnFigureAdded(figure);

    }

    /**
     * Draws this layer's contents.
     *
     * @param graphIn a graphics object to draw on
     */
    public void draw(final Graphics graphIn) {
        for (Figure figure : figures) {
            figure.draw(graphIn);
        }
    }

    /**
     * @return an image which displays all of this layer's figures
     *         painted onto a tightly-cropped region
     */
    public BufferedImage croppedImage() {

        if (contentBounds.width == 0 || contentBounds.height == 0) {
            return null;
        }

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
