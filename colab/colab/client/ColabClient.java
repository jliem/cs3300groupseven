package colab.client;

import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import colab.common.ConnectionState;
import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDataIdentifier;
import colab.common.channel.ChannelDescriptor;
import colab.common.exception.AuthenticationException;
import colab.common.exception.ConnectionDroppedException;
import colab.common.exception.NetworkException;
import colab.common.exception.UnableToConnectException;
import colab.common.exception.UserAlreadyExistsException;
import colab.common.naming.ChannelName;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import colab.common.remote.client.ColabClientInterface;
import colab.common.remote.server.ColabServerInterface;
import colab.common.remote.server.ConnectionInterface;
import colab.server.user.Password;

/**
 * The CoLab client application.
 */
public final class ColabClient extends UnicastRemoteObject implements
        ColabClientInterface {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /** The default port. */
    public static final int DEFAULT_PORT = 9040;

    private final ArrayList<ActionListener> listeners;

    private final Vector<ChannelDescriptor> channels;

    private ConnectionInterface connection;

    private ConnectionState connectionState;

    /**
     * Constructs the client application.
     *
     * @throws RemoteException
     *             if an rmi error occurs
     */
    public ColabClient() throws RemoteException {
        listeners = new ArrayList<ActionListener>();
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

        ConnectionInterface connection;
        try {
            ColabServerInterface server = (ColabServerInterface) Naming
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

    public Collection<CommunityName> getAllCommunityNames()
            throws RemoteException {
        return connection.getAllCommunityNames();
    }

    public Collection<CommunityName> getMyCommunityNames()
            throws RemoteException {
        return connection.getMyCommunityNames();
    }

    public ClientChannel joinChannel(final ChannelDescriptor desc)
            throws RemoteException {
        ClientChannel channel;
        switch (desc.getType()) {
        case CHAT:
            channel = new ClientChatChannel(desc.getName());
            connection.joinChannel(channel, desc);
            break;
        default:
            throw new IllegalArgumentException("Channel type not supported");
        }
        return channel;
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

    public void createCommunity(final CommunityName commName, Password comPass)
            throws NetworkException, UserAlreadyExistsException {


    }

    public Collection<UserName> getActiveUsers(ChannelName id) {
        // TODO Auto-generated method stub
        return null;
    }

}
