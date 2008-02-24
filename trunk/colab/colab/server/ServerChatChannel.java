package colab.server;

import java.rmi.RemoteException;
import java.util.List;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelName;
import colab.common.channel.ChatChannelData;
import colab.common.channel.ChatDataCollection;

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

        return messages.getLast(count);

    }

}