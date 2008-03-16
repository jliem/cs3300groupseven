package colab.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import colab.client.event.UserJoinedEvent;
import colab.client.event.UserLeftEvent;
import colab.client.event.UserListener;
import colab.common.channel.Channel;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;
import colab.common.remote.client.ChannelRemote;

/**
 * A client-side remote Channel object.
 */
public abstract class ClientChannel extends UnicastRemoteObject
        implements Channel, ChannelRemote {

    /**
     * The name of the channel.
     */
    private final ChannelName name;

    protected final Set<UserName> members;

    private ArrayList<UserListener> userListeners;

    public ClientChannel(final ChannelName name) throws RemoteException {

        this.name = name;

        // Create an empty collection of users
        members = new HashSet<UserName>();

        userListeners = new ArrayList<UserListener>();

    }

    public void addUserListener(UserListener ul) {
        userListeners.add(ul);
    }

    public boolean removeUserListener(UserListener ul) {
        return userListeners.remove(ul);
    }

    /** {@inheritDoc} */
    public final ChannelName getId() {
        return name;
    }

    public void userJoined(UserName userName) throws RemoteException {
        members.add(userName);

        for (UserListener ul : userListeners) {
            ul.userJoined(new UserJoinedEvent(userName));
        }
    }

    public void userLeft(UserName userName) throws RemoteException {

        boolean result = members.remove(userName);

        for (UserListener ul : userListeners) {
            ul.userLeft(new UserLeftEvent(userName));
        }

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
