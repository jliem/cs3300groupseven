package colab.client.gui;

import colab.common.channel.ChannelData;

public interface ChannelPanelListener {
    public void onMessageSent(ChannelData data);
}
