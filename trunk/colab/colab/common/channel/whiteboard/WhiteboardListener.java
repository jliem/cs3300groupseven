package colab.common.channel.whiteboard;

import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerIdentifier;

public interface WhiteboardListener {

    void onDelete(LayerIdentifier id);

    void onInsert(int offset, Layer layer);

    void onShift(LayerIdentifier id, int offset);

}
