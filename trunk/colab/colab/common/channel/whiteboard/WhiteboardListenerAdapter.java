package colab.common.channel.whiteboard;

import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerIdentifier;

public class WhiteboardListenerAdapter implements WhiteboardListener {

    /** {@inheritDoc} */
    public void onDelete(final LayerIdentifier id) {
    }

    /** {@inheritDoc} */
    public void onEdit(final LayerIdentifier id) {
    }

    /** {@inheritDoc} */
    public void onInsert(final int offset, final Layer layer) {
    }

    /** {@inheritDoc} */
    public void onShift(final LayerIdentifier id, final int offset) {
    }

}
