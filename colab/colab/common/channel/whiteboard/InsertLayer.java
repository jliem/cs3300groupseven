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
            final LayerIdentifier previous) {

        super(creator, timestamp, previous);

        this.layerId = previous;
        this.layer = null;
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Insert";
    }

    /** {@inheritDoc} */
    public void apply(Whiteboard whiteboard) throws NotApplicableException {
        whiteboard.insert(layerId, layer);
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public InsertLayer copy() {
        UserName username = (super.getCreator() != null ?
                new UserName(super.getCreator().getValue()) : null);

        LayerIdentifier li = (layerId != null ?
                new LayerIdentifier(layerId.getValue()) : null);

        InsertLayer copy = new InsertLayer(new UserName(super.getCreator().getValue()),
                super.getTimestamp(), li);

        if (layer != null) {
            copy.setLayer(layer.copy());
        }

        // Set channel id
        copy.setId(this.getId());

        return copy;
    }



}
