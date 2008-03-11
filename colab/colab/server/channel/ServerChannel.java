package colab.server.channel;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import colab.common.channel.Channel;
import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDescriptor;
import colab.common.identity.IdentitySet;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;
import colab.common.remote.client.ChannelInterface;
import colab.server.connection.Connection;
import colab.server.connection.ConnectionIdentifier;
import colab.server.event.DisconnectEvent;
import colab.server.event.DisconnectListener;

/**
 * A ServerChannel is a server-side object that represents a channel.
 *
 * @param <T> the type of {@link ChannelData} this channel uses
 */
public abstract class ServerChannel<T extends ChannelData>
        implements Channel, DisconnectListener {

    /** The name of the channel. */
    private final ChannelName name;

    /**
     * The clients that are currently active in the channel,
     * who need to be notified when a change occurs.
     */
    private IdentitySet<ConnectionIdentifier, ChannelConnection> clients;

    /**
     * Constructs a new ServerChannel.
     *
     * @param channel a description of the channel
     * @param file a file to use for persistent data storage
     * @return the created channel
     * @throws IOException if a file storage error occurs
     */
    public static ServerChannel create(final ChannelDescriptor channel,
            final File file) throws IOException {

        switch (channel.getType()) {
        case CHAT:
            return new ServerChatChannel(channel.getName(), file);
        default:
            throw new IllegalArgumentException(
                    "Channel type unsupported: " + channel.getType());
        }

    }

    /**
     * Constructs a new ServerChannel.
     *
     * @param channel a description of the channel
     * @return the created channel
     */
    public static ServerChannel create(final ChannelDescriptor channel) {

        switch (channel.getType()) {
        case CHAT:
            return new ServerChatChannel(channel.getName());
        default:
            throw new IllegalArgumentException(
                    "Channel type unsupported: " + channel.getType());
        }

    }


    /**
     * Constructs a new ServerChannel.
     *
     * @param name the name of the channel
     */
    protected ServerChannel(final ChannelName name) {

        this.name = name;

        this.clients =
            new IdentitySet<ConnectionIdentifier, ChannelConnection>();

    }

    /** {@inheritDoc} */
    public final ChannelName getId() {
        return name;
    }

    /**
     * @return a channel descriptor populated with
     *         information about this channel
     */
    public abstract ChannelDescriptor getChannelDescriptor();

    /**
     * Adds data to the channel.
     *
     * @param data data to add
     */
    public abstract void add(final T data);

    /**
     * @param count the number of elements to retrieve
     * @return the last n data elements from the channel
     */
    public abstract List<T> getLastData(final int count);

    /**
     * Adds a client to the active-clients list.
     *
     * @param client the client to add
     */
    public final void addClient(final ChannelConnection client) {

        clients.add(client);

        Connection connection = client.getConnection();

        connection.addDisconnectListener(this);

        UserName userName = connection.getUserName();
        userJoined(userName);

    }

    /**
     * Removes a client from the active-clients list.
     *
     * @param connection the connection of the client to remove
     */
    public final void removeClient(final Connection connection) {

        clients.remove(connection.getId());

        connection.removeDisconnectListener(this);

        UserName userName = connection.getUserName();
        userLeft(userName);

    }

    /**
     * @return a list of users in this Channel
     */
    public final Collection<UserName> getUsers() {
        Collection<UserName> users = new ArrayList<UserName>();
        for (ChannelConnection client : this.clients) {
            users.add(client.getConnection().getUserName());
        }
        return users;
    }

    /**
     * Informs all clients that a user has joined this channel.
     *
     * @param joinedUserName the username of the user who joined.
     */
    protected final void userJoined(final UserName joinedUserName) {

        for (final ChannelConnection client
                : this.clients.toArray(new ChannelConnection[]{})) {

            Connection connection = client.getConnection();

            ChannelInterface channelInterface =
                client.getChannelInterface();

            try {
                channelInterface.userJoined(joinedUserName);
            } catch (final RemoteException re) {
                connection.disconnect(re);
            }

        }

    }

    /**
     * Informs all clients that a user has left this channel.
     *
     * @param leftUserName the username of the user who left
     */
    protected final void userLeft(final UserName leftUserName) {

        for (final ChannelConnection client
                : this.clients.toArray(new ChannelConnection[]{})) {

            Connection connection = client.getConnection();
            ChannelInterface channelInterface =
                client.getChannelInterface();

            try {
                channelInterface.userLeft(leftUserName);
            } catch (final RemoteException re) {
                connection.disconnect(re);
            }


        }

    }

    /**
     * Sends a data element to every connected client
     * except the data's creator.
     *
     * @param data the data to send out
     */
    protected final void sendToAll(final T data) {

        for (final ChannelConnection client
                : this.clients.toArray(new ChannelConnection[]{})) {

            Connection connection = client.getConnection();
            UserName userName = connection.getUserName();

            if (!userName.equals(data.getCreator())) {

                ChannelInterface channelInterface =
                    client.getChannelInterface();

                try {
                    channelInterface.add(data);
                } catch (final RemoteException re) {
                    connection.disconnect(re);
                }

            }

        }

    }

    /** {@inheritDoc} */
    public final void handleDisconnect(final DisconnectEvent event) {
        ConnectionIdentifier connectionId = event.getConnectionId();
        clients.removeId(connectionId);
    }

}
