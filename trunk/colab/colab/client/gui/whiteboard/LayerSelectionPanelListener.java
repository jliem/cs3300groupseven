package colab.client.gui.whiteboard;

import colab.common.channel.whiteboard.layer.Layer;
import colab.common.naming.UserName;

public interface LayerSelectionPanelListener {
	void onLockRequest(Layer requested, UserName requester);
}
