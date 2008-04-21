package colab.common.channel.whiteboard;

import java.util.Date;

import colab.common.channel.ChannelData;
import colab.common.channel.document.DocumentChannelData.DocumentChannelDataType;
import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.exception.NotApplicableException;
import colab.common.naming.UserName;

/**
 * Represents a revision to a whiteboard channel.
 */
public abstract class WhiteboardChannelData extends ChannelData {

    protected LayerIdentifier layerId;

    protected WhiteboardChannelData(final UserName creator, final Date timestamp,
            final LayerIdentifier layerId) {
        super(creator, timestamp);
        this.layerId = layerId;
    }

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

    /**
     * Applies this data to a whiteboard.
     * @param whiteboard the whiteboard to use.
     * @throws NotApplicableException if the data could not be applied
     */
    public abstract void apply(final Whiteboard whiteboard)
        throws NotApplicableException;

}
