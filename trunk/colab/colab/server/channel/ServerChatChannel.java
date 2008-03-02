package colab.server.channel;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.ChannelType;
import colab.common.channel.ChatChannelData;
import colab.common.channel.ChatDataCollection;
import colab.common.naming.ChannelName;

public final class ServerChatChannel extends ServerChannel {

    /** Serialization version number. */
    private static final long serialVersionUID = 1L;

    /** The channel data. */
    private ChatDataCollection messages;

    /**
     * Constructs a new server-side chat channel.
     *
     * @param name the name of the channel
     * @throws RemoteException if an rmi error occurs
     */
    public ServerChatChannel(final ChannelName name)
            throws RemoteException {

        super(name);
        messages = new ChatDataCollection();

    }

    /** {@inheritDoc} */
    public void add(final ChannelData data) throws RemoteException {

        // Store the data
        messages.add((ChatChannelData) data);

        // Forward it to clients
        sendToAll(data);

    }

    /** {@inheritDoc} */
    public List<ChannelData> getLastData(final int count) {

        List<ChannelData> list = new ArrayList<ChannelData>();
        for(ChatChannelData ccd : messages.getLast(count)) {
            list.add(ccd);
        }
        return list;
    }

    public ChannelDescriptor getChannelDescriptor() {
        return new ChannelDescriptor(this.getId(), ChannelType.CHAT);
    }

}
