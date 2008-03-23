package colab.client;

import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.List;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.ChannelType;
import colab.common.channel.ChatChannelData;
import colab.common.channel.ChatDataSet;
import colab.common.naming.ChannelName;

public final class ClientChatChannel extends ClientChannel {

    private final static long serialVersionUID = 1;

    private int newMessages = 0;

    protected ChatDataSet messages;

    public ClientChatChannel(final ChannelName name) throws RemoteException {
        super(name);
        messages = new ChatDataSet();
    }

    public void add(final ChannelData data) throws RemoteException {
        ChatChannelData chatData = (ChatChannelData) data;
        messages.add(chatData);
        newMessages++;
        fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_FIRST,
                "Message Added"));
    }

    public ChannelDescriptor getChannelDescriptor() {
        return new ChannelDescriptor(this.getId(), ChannelType.CHAT);
    }

    public List<ChatChannelData> getLocalMessages() {
        return messages.getLast(-1);
    }

    public int getLocalNumMessages() {
        return messages.size();
    }

    public List<ChatChannelData> getNewMessages() {
        List <ChatChannelData> list = messages.getLast(newMessages);
        newMessages = 0;
        return list;
    }

}
