package colab.client;

import java.rmi.RemoteException;
import java.util.Stack;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelName;
import colab.common.channel.ChatChannelData;

public class ClientChatChannel extends ClientChannel {

    protected Stack<ChatChannelData> channelData;

    public ClientChatChannel(final ChannelName name) throws RemoteException {
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
