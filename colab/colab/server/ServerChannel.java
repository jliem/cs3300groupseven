package colab.server;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import colab.common.channel.Channel;
import colab.common.channel.ChannelData;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;
import colab.common.remote.client.ChannelInterface;

abstract class ServerChannel implements Channel {

    /**
     * The name of the channel.
     */
    private final ChannelName name;

    private Map<UserName, ChannelInterface> clients;

    public ServerChannel(final ChannelName name) throws RemoteException {

        this.name = name;

        this.clients = new HashMap<UserName, ChannelInterface>();

    }

    /** {@inheritDoc} */
    public final ChannelName getId() {
        return name;
    }

    public abstract void add(final ChannelData data) throws RemoteException;

    public abstract List<ChannelData> getLastData(final int count);

    public final void addClient(final UserName username,
            final ChannelInterface client) throws RemoteException {

        System.err.println("User " + username + " joined; "
                + "there are " + this.clients.size() + " clients connected");

        clients.put(username, client);

        userJoined(username);
    }

    public void removeClient(final UserName username) throws RemoteException {
        clients.remove(username);

        userLeft(username);
    }

    /**
     *
     * @return a list of users in this Channel
     */
    public Collection<UserName> getUsers() {
        return new HashSet<UserName>(clients.keySet());
    }

    /**
     * Informs all clients that a user has joined this channel.
     *
     * @param userName the username of the user who joined.
     * @throws RemoteException if an rmi error occurs
     */
    protected void userJoined(final UserName userName)
            throws RemoteException {
        for (final UserName un : this.clients.keySet()) {
            if (!un.equals(userName)) {
                this.clients.get(un).userJoined(userName);
            }
        }
    }

    /**
     * Informs all clients that a user has left this channel.
     *
     * @param userName the username of the user who left
     * @throws RemoteException if an rmi error occurs
     */
    protected void userLeft(UserName userName) throws RemoteException {
        for (final UserName un : this.clients.keySet()) {
            if (!un.equals(userName)) {
                this.clients.get(un).userLeft(userName);
            }
        }
    }

    protected void sendToAll(final ChannelData data) throws RemoteException {
        for (final UserName userName : this.clients.keySet()) {
            if (!userName.equals(data.getCreator())) {
                this.clients.get(userName).add(data);
            }
        }
    }

}