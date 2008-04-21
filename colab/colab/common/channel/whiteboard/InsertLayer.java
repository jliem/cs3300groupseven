package colab.common.channel.whiteboard;

import java.util.Date;

import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.exception.NotApplicableException;
import colab.common.naming.UserName;

public class InsertLayer extends WhiteboardChannelData {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private Layer layer;

    public InsertLayer(final UserName creator, final Date timestamp,
            final LayerIdentifier previous, final Layer layer) {

        super(creator, timestamp, previous);

        this.layerId = previous;
        this.layer = layer;
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Insert";
    }

    /** {@inheritDoc} */
    public void apply(Whiteboard whiteboard) throws NotApplicableException {
        whiteboard.insert(layerId, layer);
    }

}
