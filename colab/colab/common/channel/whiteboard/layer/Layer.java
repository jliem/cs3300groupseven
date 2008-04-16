package colab.common.channel.whiteboard.layer;

import colab.common.identity.Identifiable;

/**
 * A layer is a 2-dimensional image.  Multiple layers can be
 * overlayed upon each other within a whiteboard channel.
 */
public class Layer implements Identifiable<LayerIdentifier> {

    private LayerIdentifier id;

    /**
     * The user-specified label for this layer;
     * doesn't have to be unique.
     */
    private String label;

    /**
     * Constructs a new Layer.
     *
     * @param id the layer identifier
     */
    public Layer(final LayerIdentifier id) {
        this.id = id;
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
    }

}
