package colab.common.channel.whiteboard;

import java.util.Date;

import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.exception.NotApplicableException;
import colab.common.naming.UserName;

public class MoveLayer extends WhiteboardChannelData {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    protected MoveLayer(final UserName creator, final Date timestamp,
            final LayerIdentifier layerId) {

        super(creator, timestamp, layerId);
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Move";
    }

    /** {@inheritDoc} */
    public void apply(Whiteboard whiteboard) throws NotApplicableException {

        throw new UnsupportedOperationException("MoveLayer's apply not supported");

    }

}
