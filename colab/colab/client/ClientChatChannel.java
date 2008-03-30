package colab.client;

import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.List;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.ChatChannelData;
import colab.common.channel.ChatDataSet;
import colab.common.channel.type.ChatChannelType;
import colab.common.naming.ChannelName;

/**
 * A ClientChannel that handles chat-protocol channels.
 */
public final class ClientChatChannel extends ClientChannel {

    /** Serialization version number. */
    private static final long serialVersionUID = 1L;

    private int newMessages = 0;

    private ChatDataSet messages;

    /**
     * Constructs a new ClientChatChannel.
     *
     * @param name the name of the channel
     * @throws RemoteException if an rmi error occurs
     */
    public ClientChatChannel(final ChannelName name) throws RemoteException {
        super(name);
        messages = new ChatDataSet();
    }

    /** {@inheritDoc} */
    public void add(final ChannelData data) throws RemoteException {
        addLocal((ChatChannelData) data);
    }

    /**
     * Adds data to the channel.
     *
     * @param data the piece of data
     */
    public void addLocal(final ChatChannelData data) {
        messages.add(data);
        newMessages++;
        fireActionPerformed(new ActionEvent(
                this, ActionEvent.ACTION_FIRST, "Message Added"));
    }

    /** {@inheritDoc} */
    public ChannelDescriptor getChannelDescriptor() {
        return new ChannelDescriptor(this.getId(), new ChatChannelType());
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
