package colab.server.channel;

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
import colab.server.ChannelConnection;
import colab.server.connection.Connection;
import colab.server.connection.ConnectionIdentifier;
import colab.server.event.DisconnectEvent;
import colab.server.event.DisconnectListener;

public abstract class ServerChannel implements Channel, DisconnectListener {

    /**
     * The name of the channel.
     */
    private final ChannelName name;

    private IdentitySet<ConnectionIdentifier, ChannelConnection> clients;

    public static ServerChannel create(final ChannelDescriptor channel) {
        switch (channel.getType()) {
            case CHAT:
                return new ServerChatChannel(channel.getName());
            default:
                throw new IllegalArgumentException(
                        "Channel type unsupported: " + channel.getType());
        }
    }

    public ServerChannel(final ChannelName name) {

        this.name = name;

        this.clients =
            new IdentitySet<ConnectionIdentifier, ChannelConnection>();

    }

    /** {@inheritDoc} */
    public final ChannelName getId() {
        return name;
    }

    public abstract void add(final ChannelData data) throws RemoteException;

    public abstract List<ChannelData> getLastData(final int count);

    public final void addClient(final ChannelConnection client)
            throws RemoteException {

        clients.add(client);

        Connection connection = client.getConnection();

        connection.addDisconnectListener(this);

        UserName userName = connection.getUserName();
        userJoined(userName);

    }

    public void removeClient(final Connection connection)
            throws RemoteException {

        clients.remove(connection.getId());

        connection.removeDisconnectListener(this);

        UserName userName = connection.getUserName();
        userLeft(userName);

    }

    /**
     * @return a list of users in this Channel
     */
    public Collection<UserName> getUsers() {
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

        for (final ChannelConnection client : this.clients) {

            Connection connection = client.getConnection();
            UserName userName = connection.getUserName();

            if (!joinedUserName.equals(userName)) {

                ChannelInterface channelInterface =
                    client.getChannelInterface();

                try {
                    channelInterface.userJoined(userName);
                } catch (final RemoteException re) {
                    connection.disconnect(re);
                }

            }

        }

    }

    /**
     * Informs all clients that a user has left this channel.
     *
     * @param leftUserName the username of the user who left
     */
    protected final void userLeft(final UserName leftUserName) {

        for (final ChannelConnection client : this.clients) {

            Connection connection = client.getConnection();
            UserName userName = connection.getUserName();

            if (!leftUserName.equals(userName)) {

                ChannelInterface channelInterface =
                    client.getChannelInterface();

                try {
                    channelInterface.userLeft(userName);
                } catch (final RemoteException re) {
                    connection.disconnect(re);
                }

            }

        }

    }

    protected final void sendToAll(final ChannelData data) {

        for (final ChannelConnection client : this.clients) {

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
    public void handleDisconnect(final DisconnectEvent event) {
        ConnectionIdentifier connectionId = event.getConnectionId();
        clients.removeId(connectionId);
    }

}
