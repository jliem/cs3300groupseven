package colab.common.channel.whiteboard.layer;

import colab.common.identity.IntegerIdentifier;

/**
 * An Identifier used to identify whiteboard layers.
 */
public class LayerIdentifier extends IntegerIdentifier {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs a layer identifier with a given value.
     *
     * @param id the value of the identifying integer
     */
    public LayerIdentifier(final Integer id) {
        super(id);
    }

    /**
     * Constructs a layer identifier with the
     * value of an IntegerIdentifier.
     *
     * @param otherId the identifier to copy
     */
    public LayerIdentifier(final IntegerIdentifier otherId) {
        super(otherId.getValue());
    }

}
