package colab.client;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.List;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.chat.ChatChannelData;
import colab.common.channel.type.ChatChannelType;
import colab.common.naming.ChannelName;

/**
 * A ClientChannel that handles chat-protocol channels.
 */
public final class ClientChatChannel extends ClientChannel<ChatChannelData> {

    /** Serialization version number. */
    private static final long serialVersionUID = 1L;

    private int newMessages = 0;

    private ChannelDataSet<ChatChannelData> messages;

    /**
     * Constructs a new ClientChatChannel.
     *
     * @param name the name of the channel
     * @throws RemoteException if an rmi error occurs
     */
    public ClientChatChannel(final ChannelName name) throws RemoteException {
        super(name);
        messages = new ChannelDataSet<ChatChannelData>();
    }

    public ChannelDataSet<ChatChannelData> getChannelData() {
        return messages;
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

    public void export(final File file) throws IOException {

        PrintWriter writer = new PrintWriter(new FileOutputStream(file));

        try {
            for (final ChatChannelData message : getLocalMessages()) {
                writer.println(message.getMessageString(true));
            }
        } finally {
            writer.close();
        }

    }

}
