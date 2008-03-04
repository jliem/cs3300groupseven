package colab.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

import colab.common.channel.Channel;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;
import colab.common.remote.client.ChannelInterface;

/**
 * A client-side remote Channel object.
 */
abstract class ClientChannel extends UnicastRemoteObject
        implements Channel, ChannelInterface {

    /**
     * The name of the channel.
     */
    private final ChannelName name;

    protected final Set<UserName> members;

    public ClientChannel(final ChannelName name) throws RemoteException {

        this.name = name;

        // Create an empty collection of users
        members = new HashSet<UserName>();

    }

    /** {@inheritDoc} */
    public final ChannelName getId() {
        return name;
    }

    public void userJoined(UserName userName) throws RemoteException {
        members.add(userName);
    }

    public void userLeft(UserName userName) throws RemoteException {

        boolean result = members.remove(userName);

        // Check that remove was successful
        if (!result) {
            throw new IllegalStateException("Could not remove user "
                + userName.toString() + " from members list in ClientChannel");
        }

    }

    public final Set<UserName> getMembers() {
        return this.members;
    }
}
