package colab.common.channel.whiteboard;

import java.util.Date;

import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.exception.NotApplicableException;
import colab.common.naming.UserName;

/**
 * A command to delete a layer from a whiteboard.
 */
public class DeleteLayer extends WhiteboardChannelData {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs an empty DeleteLayer.
     */
    public DeleteLayer() {
    }

    /**
     * Constructs a new DeleteLayer.
     *
     * @param creator the user performing the action
     * @param timestamp the time at which it is deleted
     * @param layerId the id of the layer to delete
     */
    public DeleteLayer(final UserName creator, final Date timestamp,
            final LayerIdentifier layerId) {

        super(creator, timestamp, layerId);

    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Delete";
    }

    /** {@inheritDoc} */
    public void apply(final Whiteboard whiteboard)
            throws NotApplicableException {
        whiteboard.delete(getLayerId());
    }

    public DeleteLayer copy() {

        UserName username = null;
        if (super.getCreator() != null) {
            username = new UserName(super.getCreator().getValue());
        }

        LayerIdentifier li = null;
        if (getLayerId() != null) {
            li = new LayerIdentifier(getLayerId().getValue());
        }

        DeleteLayer copy = new DeleteLayer(username, super.getTimestamp(), li);

        return copy;
    }

}
