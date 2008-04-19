package colab.common.channel.whiteboard.layer;

import java.awt.Graphics;
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

    /**
     * Constructs a new Layer.
     *
     * @param id the layer identifier
     */
    public Layer(final LayerIdentifier id) {
        this.id = id;
        this.listeners = new ArrayList<LayerListener>();
        this.figures = new LinkedList<Figure>();
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
        this.figures.add(figure);
        this.fireOnFigureAdded(figure);
    }

    public void draw(final Graphics graphIn) {
        for (Figure figure : figures) {
            figure.draw(graphIn);
        }
    }

}
