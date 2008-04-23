package colab.common.channel.whiteboard;

import java.util.Date;

import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.exception.NotApplicableException;
import colab.common.naming.UserName;

public class DeleteLayer extends WhiteboardChannelData {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs an empty DeleteLayer.
     */
    public DeleteLayer() {
    }

    public DeleteLayer(final UserName creator, final Date timestamp,
            final LayerIdentifier layerId) {

        super(creator, timestamp, layerId);

    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Delete";
    }

    /** {@inheritDoc} */
    public void apply(Whiteboard whiteboard) throws NotApplicableException {
        whiteboard.delete(layerId);
    }

}
