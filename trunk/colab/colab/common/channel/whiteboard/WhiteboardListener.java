package colab.common.channel.whiteboard;

import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerIdentifier;

public interface WhiteboardListener {
    public void onDelete(LayerIdentifier id);
    public void onInsert(int offset, Layer layer);
    public void onShift(LayerIdentifier id, int offset);
}
