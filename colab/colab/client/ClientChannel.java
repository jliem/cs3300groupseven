package colab.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import colab.common.channel.Channel;
import colab.common.event.UserJoinedEvent;
import colab.common.event.UserLeftEvent;
import colab.common.event.UserListener;
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

    private ArrayList<ActionListener> listeners;

    protected final Set<UserName> members;

    private List<UserListener> userListeners;

    public ClientChannel(final ChannelName name) throws RemoteException {

        this.name = name;

        // Create an empty collection of users
        members = new HashSet<UserName>();

        listeners = new ArrayList<ActionListener>();
        userListeners = new ArrayList<UserListener>();

    }

    public final void addUserListener(final UserListener listener) {
        userListeners.add(listener);
    }

    public final boolean removeUserListener(final UserListener listener) {
        return userListeners.remove(listener);
    }

    /** {@inheritDoc} */
    public final ChannelName getId() {
        return name;
    }

    /** {@inheritDoc} */
    public final void handleUserEvent(final UserJoinedEvent event)
            throws RemoteException {

        members.add(event.getUserName());

        for (UserListener listener : userListeners) {
            listener.handleUserEvent(event);
        }

    }

    /** {@inheritDoc} */
    public final void handleUserEvent(final UserLeftEvent event)
            throws RemoteException {

        members.remove(event.getUserName());

        for (UserListener listener : userListeners) {
            listener.handleUserEvent(event);
        }

    }

    /**
     * @return the names of the users who are currently in this channel
     */
    public final Set<UserName> getMembers() {
        return this.members;
    }

    protected void fireActionPerformed(final ActionEvent e) {
        for (ActionListener l : listeners) {
            l.actionPerformed(e);
        }
    }

    public void addActionListener(final ActionListener l) {
        listeners.add(l);
    }

    public void removeActionListener(final ActionListener l) {
        listeners.remove(l);
    }

}
