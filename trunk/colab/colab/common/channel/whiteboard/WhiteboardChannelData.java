package colab.common.channel.whiteboard;

import colab.common.channel.ChannelData;
import colab.common.channel.whiteboard.layer.LayerIdentifier;

/**
 * Represents a revision to a whiteboard channel.
 */
public abstract class WhiteboardChannelData extends ChannelData {

    private LayerIdentifier layerId;

    /**
     * @return the id of the layer being edited
     */
    public LayerIdentifier getLayerId() {
        return layerId;
    }

    /**
     * @param layerId the id of the layer being edited
     */
    public void setLayerId(final LayerIdentifier layerId) {
        this.layerId = layerId;
    }

}
