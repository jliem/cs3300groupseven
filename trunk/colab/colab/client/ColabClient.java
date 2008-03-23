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
import colab.common.exception.AuthenticationException;
import colab.common.exception.CommunityAlreadyExistsException;
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
 * The CoLab client application.
 */
public final class ColabClient extends UnicastRemoteObject implements
        ColabClientRemote {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /** The default port. */
    public static final int DEFAULT_PORT = 9040;

    //private final List<ActionListener> listeners;

    private final Vector<ChannelDescriptor> channels;

    private ConnectionRemote connection;

    private ConnectionState connectionState;

    /**
     * Constructs the client application.
     *
     * @throws RemoteException
     *             if an rmi error occurs
     */
    public ColabClient() throws RemoteException {
        //listeners = new ArrayList<ActionListener>();
        channels = new Vector<ChannelDescriptor>();
        connectionState = ConnectionState.DISCONNECTED;
    }

    /**
     * Receives the server address and attempts to connect to the server.
     *
     * @param serverAddress
     *            the address of the server
     * @throws UnableToConnectException
     *             if connection fails
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

    /**
     * Receives the username and password from the GUI fields and checks to see
     * if there is an existing corresponding pair. If the username exists but
     * the password is incorrect, a ValidationException is thrown. If the
     * username does not exist, then the GUI asks the user if a new user should
     * be added.
     *
     * @param username
     *            received from the GUI text field
     * @param password
     *            received from the GUI password field
     * @param serverAddress
     *            address received from the GUI text field
     * @throws NetworkException
     *             if connection fails
     * @throws AuthenticationException
     *             if user credentials are wrong
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
     * @throws NetworkException if a network I/O error occurs
     * @throws UserAlreadyExistsException if a user with the specified
     *                                    name already exists on the server
     */
    public void createUser(final UserName userName, final char[] password)
            throws NetworkException, UserAlreadyExistsException {

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
     * @throws NetworkException if a network I/O error occurs
     * @throws AuthenticationException if the server rejected the login
     *                                 because the user is not a member
     *                                 of the community
     */
    public void loginCommmunity(final CommunityName communityName)
            throws NetworkException, AuthenticationException {

        // Must not yet be logged in to a community
        if (this.connectionState.hasCommunityLogin()) {
            System.err.println("[Connection] Attempt to perform community "
                    + "login on connection in '" + this.connectionState
                    + "' state");
            throw new IllegalStateException();
        }

        try {
            this.connection.logIn(communityName, null);
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
     * Retrieves the names of all of the communities on the server.
     *
     * @return a collection containing the name of every community
     * @throws NetworkException if a network I/O error occurs
     */
    public Collection<CommunityName> getAllCommunityNames()
            throws NetworkException {

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
     * @throws NetworkException if a network I/O error occurs
     */
    public Collection<CommunityName> getMyCommunityNames()
            throws NetworkException {

        try {
            return connection.getMyCommunityNames();
        } catch (final RemoteException remoteException) {
            throw new ConnectionDroppedException(remoteException);
        }

    }

    public ClientChannel joinChannel(final ChannelDescriptor desc)
            throws RemoteException {

        ClientChannel channel;
        switch (desc.getType()) {
        case CHAT:
            channel = new ClientChatChannel(desc.getName());
            break;
        default:
            throw new IllegalArgumentException("Channel type not supported");
        }
        connection.joinChannel(channel, desc);
        return channel;

    }

    public void leaveChannel(final ChannelDescriptor desc)
        throws RemoteException {

        connection.leaveChannel(desc.getName());
    }

    /** {@inheritDoc} */
    public void channelAdded(final ChannelDescriptor channelDescriptor)
            throws RemoteException {
        channels.add(channelDescriptor);
    }

    public Vector<ChannelDescriptor> getChannels() {
        return channels;
    }

    public void add(final ChannelName channelName, final ChannelData data)
            throws RemoteException {

        ChannelDataIdentifier id = connection.add(channelName, data);
        data.setId(id);

    }

    public List<ChannelData> getLastData(final ChannelName channelName,
            final int count) throws RemoteException {

        return connection.getLastData(channelName, count);

    }

    public void logOutUser() throws ConnectionDroppedException {

        this.connectionState = ConnectionState.CONNECTED;

        try {
            connection.logOutUser();
        } catch (RemoteException remoteException) {
            throw new ConnectionDroppedException(remoteException);
        }

    }

    public void logOutCommunity() throws ConnectionDroppedException {

        this.connectionState = ConnectionState.LOGGED_IN;

        try {
            connection.logOutCommunity();
        } catch (RemoteException remoteException) {
            throw new ConnectionDroppedException(remoteException);
        }

    }

    public void createCommunity(final CommunityName commName,
            final Password comPass) throws NetworkException,
            CommunityAlreadyExistsException {

        // TODO:

    }

    /**
     * Gets active users in a channel.
     * @param channelName the channel to look up
     * @return a list of all active users
     * @throws RemoteException if a RemoteException occurs
     */
    public Collection<UserName> getActiveUsers(ChannelName channelName)
        throws RemoteException {

        return connection.getActiveUsers(channelName);
    }

    /**
     * Completely closes all threads, windows, etc. associated
     * with the client.
     */
    public void exitProgram() {

        try {
            logOutUser();
        } catch (ConnectionDroppedException e1) {
            if (DebugManager.EXIT_EXCEPTIONS) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            if (DebugManager.EXIT_EXCEPTIONS) {
                e.printStackTrace();
            }
        }

        System.exit(0);
    }

}
