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
import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.ChannelDescriptor;
import colab.common.event.UserJoinedEvent;
import colab.common.event.UserLeftEvent;
import colab.common.event.UserListener;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;
import colab.common.remote.client.ChannelRemote;

/**
 * A client-side remote Channel object.
 *
 * @param <T> the type of data maintained by this channel
 */
public abstract class ClientChannel<T extends ChannelData>
        extends UnicastRemoteObject
        implements Channel, ChannelRemote {

    /** The name of the channel. */
    private final ChannelName name;

    private List<ActionListener> listeners;

    /** Active users in the channel. */
    private final Set<UserName> members;

    private List<UserListener> userListeners;

    /**
     * Constructs a new ClientChannel.
     *
     * @param name the name of the channel
     * @throws RemoteException if an rmi error occurs
     */
    public ClientChannel(final ChannelName name) throws RemoteException {

        this.name = name;

        // Create an empty collection of users
        members = new HashSet<UserName>();

        listeners = new ArrayList<ActionListener>();
        userListeners = new ArrayList<UserListener>();

    }

    /**
     * @return the descriptor for this channel.
     */
    public abstract ChannelDescriptor getChannelDescriptor();

    /**
     * Get the channel data.
     *
     * @return the channel data
     */
    public abstract ChannelDataSet<T> getChannelData();

    /**
     * Adds a listener who is concerned with user
     * events (who joined/left the channel).
     *
     * @param listener the listener to add
     */
    public final void addUserListener(final UserListener listener) {
        userListeners.add(listener);
    }

    /**
     * Removes a user listener.
     *
     * @param listener the listener to remove
     */
    public final void removeUserListener(final UserListener listener) {
        userListeners.remove(listener);
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

    /**
     * Notifies all listeners of an event.
     *
     * @param event the event being fired
     */
    protected final void fireActionPerformed(final ActionEvent event) {
        for (final ActionListener listener : listeners) {
            listener.actionPerformed(event);
        }
    }

    /**
     * Adds a generic listener who listens on some kind of event.
     *
     * @param listener the listener to add
     */
    public final void addActionListener(final ActionListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a generic listener.
     *
     * @param listener the listener to remove
     */
    public final void removeActionListener(final ActionListener listener) {
        listeners.remove(listener);
    }

}
