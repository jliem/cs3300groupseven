package colab.common.channel;

import java.util.Stack;

import colab.client.ClientChannel;

public class ChatChannel extends ClientChannel {

    protected Stack<ChatChannelData> channelData;

    public ChatChannel(final ChannelName name) {
        super(name);
        channelData = new Stack<ChatChannelData>();
    }

    @Override
    public void add(final ChannelData data) {

        ChatChannelData chatData = (ChatChannelData)data;

        channelData.push(chatData);
    }

    @Override
    public boolean remove(final ChannelData data) {
        return channelData.remove(data);
    }


}
