package colab.common.channel.whiteboard;

import java.util.Date;

import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.exception.NotApplicableException;
import colab.common.naming.UserName;

public class MoveLayer extends WhiteboardChannelData {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs an empty MoveLayer.
     */
    public MoveLayer() {
    }

    public MoveLayer(final UserName creator, final Date timestamp,
            final LayerIdentifier layerId) {

        super(creator, timestamp, layerId);
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Move";
    }

    /** {@inheritDoc} */
    public void apply(final Whiteboard whiteboard)
            throws NotApplicableException {

        throw new UnsupportedOperationException(
                "MoveLayer's apply not supported");

    }

    public MoveLayer copy() {
        UserName username = null;
        if (super.getCreator() != null) {
            username = new UserName(super.getCreator().getValue());
        }

        LayerIdentifier li = null;
        if (getLayerId() != null) {
            li = new LayerIdentifier(getLayerId().getValue());
        }

        MoveLayer copy = new MoveLayer(username, super.getTimestamp(), li);

        return copy;
    }

}
