package colab.client;

import java.rmi.RemoteException;

import colab.client.remote.ChannelInterface;
import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.ChannelName;
import colab.common.channel.ChannelType;
import colab.common.channel.ChatChannelData;
import colab.common.channel.ChatDataCollection;

public class ClientChatChannel extends ClientChannel implements ChannelInterface {

    protected ChatDataCollection messages;

    public ClientChatChannel(final ChannelName name) throws RemoteException {
        super(name);
        messages = new ChatDataCollection();
    }

    public void add(final ChannelData data) throws RemoteException {
        ChatChannelData chatData = (ChatChannelData) data;
        messages.add(chatData);
    }

    @Override
    public ChannelDescriptor getChannelDescriptor() {
        return new ChannelDescriptor(this.getId(), ChannelType.CHAT);
    }

}
