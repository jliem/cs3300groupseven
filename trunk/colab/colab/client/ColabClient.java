package colab.client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import colab.common.ConnectionState;
import colab.common.DebugManager;
import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDataIdentifier;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.type.ChannelType;
import colab.common.event.channel.ChannelEvent;
import colab.common.event.channel.ChannelListener;
import colab.common.exception.AuthenticationException;
import colab.common.exception.ChannelAlreadyExistsException;
import colab.common.exception.CommunityAlreadyExistsException;
import colab.common.exception.CommunityDoesNotExistException;
import colab.common.exception.ConnectionDroppedException;
import colab.common.exception.NetworkException;
import colab.common.exception.UnableToConnectException;
import colab.common.exception.UserAlreadyExistsException;
import colab.common.naming.ChannelName;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import colab.common.remote.client.ColabClientRemote;
import colab.common.remote.server.ColabServerRemote;
import colab.common.remote.server.ConnectionRemote;
import colab.server.user.Password;

/**
 * The CoLab client application which is used to make requests to the server.
 */
public class ColabClient extends UnicastRemoteObject
        implements ColabClientRemote {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /** The default port. */
    public static final int DEFAULT_PORT = 9040;

    /** All channels in the community to which the client is connected. */
    private final Vector<ChannelDescriptor> channels;

    private Vector<ChannelListener> channelListeners;

    private ConnectionRemote connection;

    private ConnectionState connectionState;

    /**
     * Constructs the client application.
     *
     * @throws RemoteException if an rmi error occurs
     */
    public ColabClient() throws RemoteException {
        //listeners = new ArrayList<ActionListener>();
        channels = new Vector<ChannelDescriptor>();
        channelListeners = new Vector<ChannelListener>();

        connectionState = ConnectionState.DISCONNECTED;
    }

    /**
     * Receives the server address and attempts to connect to the server.
     *
     * @param serverAddress the address of the server
     * @throws UnableToConnectException if connection fails
     */
    public void connect(final String serverAddress)
            throws UnableToConnectException {

        ConnectionRemote connection;
        try {
            ColabServerRemote server = (ColabServerRemote) Naming
                    .lookup("//" + serverAddress + "/COLAB_SERVER");
            connection = server.connect(this);
        } catch (final Exception e) {
            throw new UnableToConnectException();
        } finally {
            this.connectionState = ConnectionState.DISCONNECTED;
        }

        this.connectionState = ConnectionState.CONNECTED;
        this.connection = connection;

    }

    /** {@inheritDoc} */
    public void ping() throws RemoteException {
    }

    /**
     * Receives the username and password from the GUI fields and
     * checks to see if there is an existing corresponding pair.
     *
     * If the username exists but the password is incorrect, a
     * ValidationException is thrown.
     *
     * If the username does not exist, then the GUI asks the user
     * if a new user should be added.
     *
     * @param username received from the GUI text field
     * @param password received from the GUI password field
     * @param serverAddress address received from the GUI text field
     * @throws NetworkException if connection fails
     * @throws AuthenticationException if user credentials are wrong
     */

    public void loginUser(final String username, final char[] password,
            final String serverAddress) throws NetworkException,
            AuthenticationException {

        // Must not be already logged in
        if (this.connectionState.hasUserLogin()) {
            System.err.println("[ColabClient] Attempt to perform user "
                    + "login on connection in '" + this.connectionState
                    + "' state");
            throw new IllegalStateException();
        }

        connect(serverAddress);

        try {
            this.connection.logIn(new UserName(username), password);
        } catch (final ServerException serverException) {
            try {
                throw serverException.getCause().getCause();
            } catch (final AuthenticationException authenticationException) {
                throw authenticationException;
            } catch (final Throwable t) {
                throw new ConnectionDroppedException(serverException);
            }
        } catch (final RemoteException remoteException) {
            throw new ConnectionDroppedException(remoteException);
        } finally {
            this.connectionState = ConnectionState.CONNECTED;
        }

        this.connectionState = ConnectionState.LOGGED_IN;

    }

    /**
     * Sends a request to the server to create a new user account.
     *
     * @param userName the name of the user to create
     * @param password the desired password for the new user
     * @throws ConnectionDroppedException if the connection is lost
     * @throws UserAlreadyExistsException if a user with the specified
     *                                    name already exists on the server
     */
    public void createUser(final UserName userName, final char[] password)
            throws ConnectionDroppedException, UserAlreadyExistsException {

        try {
            connection.createUser(userName.getValue(), password);
        } catch (final ServerException serverException) {
            try {
                throw serverException.getCause().getCause();
            } catch (final UserAlreadyExistsException userExistsException) {
                throw userExistsException;
            } catch (final Throwable t) {
                throw new ConnectionDroppedException(serverException);
            }
        } catch (final RemoteException remoteException) {
            throw new ConnectionDroppedException(remoteException);
        }

    }

    /**
     * Logs into a community.
     *
     * @param communityName the name of community to log in to
     * @param password the community's password
     * @throws ConnectionDroppedException if the connection is lost
     * @throws AuthenticationException if the server rejected the login
     *                                 because the user is not a member
     *                                 of the community
     */
    public void loginCommunity(final CommunityName communityName,
            final char[] password) throws ConnectionDroppedException,
            AuthenticationException {


        // Must not yet be logged in to a community
        if (this.connectionState.hasCommunityLogin()) {
            System.err.println("[ColabClient] Attempt to perform community "
                    + "login on connection in '" + this.connectionState
                    + "' state");
            throw new IllegalStateException();
        }

        // Clear existing list of channels
        // Should be done on log out already, but do it here
        // also just in case.
        this.clearJoinableChannels();

        try {
            this.connection.logIn(communityName, password);
        } catch (final ServerException serverException) {
            try {
                throw serverException.getCause().getCause();
            } catch (final AuthenticationException authenticationException) {
                throw authenticationException;
            } catch (final Throwable t) {
                throw new ConnectionDroppedException(serverException);
            }
        } catch (final RemoteException remoteException) {
            throw new ConnectionDroppedException(remoteException);
        } finally {
            this.connectionState = ConnectionState.LOGGED_IN;
        }

        this.connectionState = ConnectionState.ACTIVE;
    }
    /**
     * Logs into a community.
     *
     * @param communityName the name of community to log in to
     * @throws ConnectionDroppedException if the connection is lost
     * @throws AuthenticationException if the server rejected the login
     *                                 because the user is not a member
     *                                 of the community
     */
    public void loginCommmunity(final CommunityName communityName)
            throws ConnectionDroppedException, AuthenticationException {

        this.loginCommunity(communityName, null);
    }

    /**
     * Retrieves the names of all of the communities on the server.
     *
     * @return a collection containing the name of every community
     * @throws ConnectionDroppedException if the connection is lost
     */
    public Collection<CommunityName> getAllCommunityNames()
            throws ConnectionDroppedException {

        try {
            return connection.getAllCommunityNames();
        } catch (final RemoteException remoteException) {
            throw new ConnectionDroppedException(remoteException);
        }

    }

    /**
     * Retrieves the names of the communities on the server
     * of which the currently logged-in user is a member.
     *
     * @return a collection containing the name of communities
     * @throws ConnectionDroppedException if the connection is lost
     */
    public Collection<CommunityName> getMyCommunityNames()
            throws ConnectionDroppedException {

        try {
            return connection.getMyCommunityNames();
        } catch (final RemoteException remoteException) {
            throw new ConnectionDroppedException(remoteException);
        }

    }

    /**
     * Join a channel.
     *
     * @param desc the channel descriptor
     * @return a reference to the newly created ClientChannel
     * @throws RemoteException if an rmi error ocurs
     */
    public ClientChannel joinChannel(final ChannelDescriptor desc)
            throws RemoteException {

        ChannelType type = desc.getType();
        ClientChannel channel = type.createClientChannel(desc.getName());

        connection.joinChannel(channel, desc);
        return channel;

    }

    /**
     * Inform the server that the user is no longer active
     * in the given channel.
     *
     * @param descriptor the channel to leave
     * @throws RemoteException if an rmi error occurs
     */
    public void leaveChannel(final ChannelDescriptor descriptor)
            throws RemoteException {

        if (connection != null) {
            connection.leaveChannel(descriptor.getName());
        }

    }

    /** {@inheritDoc} */
    public void channelAdded(final ChannelDescriptor channelDescriptor)
            throws RemoteException {
        channels.add(channelDescriptor);

        this.fireChannelEvent(new ChannelEvent(channelDescriptor));

    }

    /**
     * Adds a channel listener.
     *
     * @param listener the listener
     */
    public void addChannelListener(final ChannelListener listener) {
        channelListeners.add(listener);
    }

    /**
     * Removes a channel listener.
     *
     * @param listener the listener
     * @return true if the remove was successful, false otherwise
     */
    public boolean removeChannelListener(final ChannelListener listener) {
        return channelListeners.remove(listener);
    }

    /**
     * Returns an unsorted list of all channels this client
     * can access.
     *
     * @return a vector of channels
     */
    public Vector<ChannelDescriptor> getChannels() {
        return channels;
    }

    /**
     * Sends a new piece of channel data to the server.
     *
     * @param channelName the name of the channel to add to
     * @param data the data to add
     * @throws ConnectionDroppedException if the connection is lost
     */
    public void add(final ChannelName channelName, final ChannelData data)
            throws ConnectionDroppedException {

        ChannelDataIdentifier id;
        try {
           id = connection.add(channelName, data);
        } catch (final RemoteException remoteException) {
            DebugManager.remote(remoteException);
            throw new ConnectionDroppedException(remoteException);
        }
        data.setId(id);

    }

    /**
     * Retrieves the last n data items posted to a given channel.
     *
     * @param channelName the name of the channel from which to fetch data
     * @param count the number of items requested
     * @return the last n data items
     * @throws ConnectionDroppedException if the connection is lost
     */
    public List<ChannelData> getLastData(final ChannelName channelName,
            final int count) throws ConnectionDroppedException {

        try {
            return connection.getLastData(channelName, count);
        } catch (final RemoteException remoteException) {
            DebugManager.remote(remoteException);
            throw new ConnectionDroppedException(remoteException);
        }

    }

    /**
     * Logs out of the user account.
     *
     * @throws ConnectionDroppedException if the connection is lost
     */
    public void logOutUser() throws ConnectionDroppedException {

        this.connectionState = ConnectionState.CONNECTED;

        try {
            if (connection != null) {
                connection.logOutUser();
            }
        } catch (RemoteException remoteException) {
            throw new ConnectionDroppedException(remoteException);
        }

    }

    /**
     * Logs out of a community.
     *
     * @throws ConnectionDroppedException if the connection is lost
     */
    public void logOutCommunity() throws ConnectionDroppedException {

        this.connectionState = ConnectionState.LOGGED_IN;

        // Clear our list of channels
        this.clearJoinableChannels();

        try {
            connection.logOutCommunity();
        } catch (RemoteException remoteException) {
            throw new ConnectionDroppedException(remoteException);
        }

    }

    /**
     * Checks whether the logged in user is a member of a given community.
     *
     * @param communityName the community name
     * @return true if the user is a member of the community, false otherwise
     * @throws CommunityDoesNotExistException
     * @throws RemoteException if an rmi error occurs
     * @throws CommunityDoesNotExistException if the community did not exist
     */
    public boolean isMember(final CommunityName communityName)
            throws RemoteException, CommunityDoesNotExistException {

        return connection.isMember(communityName);

    }

    /**
     * Checks whether the logged in user is a moderator of a given community.
     *
     * @param communityName the community name
     * @return true if the user is a moderator of the community, false otherwise
     * @throws CommunityDoesNotExistException
     * @throws RemoteException if an rmi error occurs
     * @throws CommunityDoesNotExistException if the community did not exist
     */
    public boolean isModerator(final CommunityName communityName)
            throws RemoteException, CommunityDoesNotExistException {

        return connection.isModerator(communityName);

    }

    /**
     * Create a new community on the server.
     *
     * @param name the community name
     * @param password the community password
     * @throws CommunityAlreadyExistsException if a community with the
     *                                         given name already exists
     * @throws ConnectionDroppedException if the connection is lost
     */
    public void createCommunity(final CommunityName name,
            final char[] password) throws CommunityAlreadyExistsException,
            ConnectionDroppedException {

        try {
            connection.createCommunity(name.getValue(), password);
        } catch (final ServerException serverException) {
            try {
                throw serverException.getCause().getCause();
            } catch (final CommunityAlreadyExistsException e) {
                throw e;
            } catch (final Throwable t) {
                throw new ConnectionDroppedException(serverException);
            }
        } catch (final RemoteException remoteException) {
            throw new ConnectionDroppedException(remoteException);
        }

    }

    /**
     * Creates a new channel on the server in the
     * currently logged-in community.
     *
     * @param channelDesc channel descriptor
     * @throws ChannelAlreadyExistsException if the channel already exists
     * @throws ConnectionDroppedException if the connection is lost
     */
    public void createChannel(final ChannelDescriptor channelDesc)
            throws ChannelAlreadyExistsException, ConnectionDroppedException {

        try {
            connection.createChannel(channelDesc);
        } catch (final ServerException serverException) {
            try {
                throw serverException.getCause().getCause();
            } catch (final ChannelAlreadyExistsException e) {
                throw e;
            } catch (final Throwable t) {
                throw new ConnectionDroppedException(serverException);
            }
        } catch (final RemoteException remoteException) {
            throw new ConnectionDroppedException(remoteException);
        } finally {
            this.connectionState = ConnectionState.LOGGED_IN;
        }

    }

    /**
     * Returns the users which are members of this community.
     *
     * @param communityName the name of the community
     * @return a collection containing every user of this community
     * @throws CommunityDoesNotExistException if the community did not exist
     * @throws RemoteException if a remote exception occurs
     */
    public Collection<UserName> getMembers(final CommunityName communityName)
        throws CommunityDoesNotExistException, RemoteException {

        if (connection != null) {
            return connection.getMembers(communityName);
        }

        return null;
    }

    /**
     * Removes a user as a member from the community.
     *
     * @param userName the username to remove
     * @param communityName the name of the community
     * @return true if the remove was successful, false otherwise.
     * @throws CommunityDoesNotExistException if the community does not exist
     * @throws RemoteException if a remote exception occurs
     */
    public boolean removeMember(final UserName userName,
            final CommunityName communityName)
            throws CommunityDoesNotExistException, RemoteException {

        if (connection != null) {
            return connection.removeMember(userName, communityName);
        }

        return false;
    }

    /**
     * Changes the password of a community.
     * @param communityName the community name
     * @param password the new password
     * @throws CommunityDoesNotExistException if the community does not exist
     * @throws RemoteException if a remote exception occurs
     */
    public void changePassword(final CommunityName communityName,
            final Password password)
        throws CommunityDoesNotExistException, RemoteException {


        if (connection != null) {
            connection.changePassword(communityName, password);
        }
    }


    /**
     * Gets active users in a channel.
     *
     * @param channelName the channel to look up
     * @return a list of all active users
     * @throws RemoteException if an rmi error occurs
     */
    public Collection<UserName> getActiveUsers(final ChannelName channelName)
            throws RemoteException {

        if (connection != null) {
            return connection.getActiveUsers(channelName);
        }

        return null;

    }

    /**
     * Checks whether a user has logged into the program.
     *
     * @return true if the user has logged in
     * @throws RemoteException if an rmi error occurs
     */
    public boolean hasUserLogin() throws RemoteException {
        // If no connection, there's definitely no user login
        return (connection != null && connection.hasUserLogin());
    }

    /**
     * Completely closes all threads, windows, etc. associated
     * with the client.
     */
    public void exitProgram() {

        try {
            if (this.hasUserLogin()) {
                logOutUser();
            }
        } catch (final Exception e) {
            DebugManager.windowClose(e);
        }

        System.exit(0);

    }

    /**
     * Notifies all channel listeners of a new event.
     * @param channelEvent the channel event
     */
    private void fireChannelEvent(final ChannelEvent channelEvent) {
        for (ChannelListener cl : channelListeners) {
            cl.handleChannelEvent(channelEvent);
        }
    }

    /**
     * Clears the list of channels this client could potentially join.
     */
    private void clearJoinableChannels() {
        channels.clear();
    }

}
