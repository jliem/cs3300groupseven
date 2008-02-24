package colab.client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;

import colab.client.remote.ColabClientInterface;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.ChannelName;
import colab.common.community.CommunityName;
import colab.common.exception.network.ConnectionDroppedException;
import colab.common.exception.network.NetworkException;
import colab.common.exception.network.UnableToConnectException;
import colab.common.exception.remote.AuthenticationException;
import colab.common.user.UserName;
import colab.server.remote.ColabServerInterface;
import colab.server.remote.ConnectionInterface;

/**
 * The CoLab client application.
 */
public final class ColabClient extends UnicastRemoteObject
        implements ColabClientInterface {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /** The default port. */
    public static final int DEFAULT_PORT = 9040;

    private ConnectionInterface connection;

    /**
     * Constructs the client application.
     *
     * @throws RemoteException if an rmi error occurs
     */
    public ColabClient() throws RemoteException {

    }

    /**
     * Receives the server address and attempts to
     * connect to the server.
     *
     * @param serverAddress the address of the server
     * @throws UnableToConnectException if connection fails
     */
    public void connect(final String serverAddress)
            throws UnableToConnectException {

        ConnectionInterface connection;
        try {
            ColabServerInterface server = (ColabServerInterface) Naming.lookup(
                    "//" + serverAddress + "/COLAB_SERVER");
            connection = server.connect(this);
        } catch (final Exception e) {
            throw new UnableToConnectException();
        }

        this.connection = connection;

    }

    /**
     * Receives the username and password from the GUI fields and checks to
     * see if there is an existing corresponding pair.  If the username
     * exists but the password is incorrect, a ValidationException is thrown.
     * If the username does not exist, then the GUI asks the user if a new
     * user should be added.
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

        connect(serverAddress);

        try {
            this.connection.logIn(new UserName(username), password);
        } catch (final AuthenticationException ae) {
            throw ae;
        } catch (final RemoteException re) {
            throw new ConnectionDroppedException(re);
        }

    }

    public Collection<CommunityName> getAllCommunityNames() throws RemoteException{
        return connection.getAllCommunityNames();
     }

    public Collection<CommunityName> getMyCommunityNames() throws RemoteException{
        return connection.getMyCommunityNames();
    }

    public ClientChannel joinChannel(ChannelName name){
        return null;

    }

    public void channelAdded(ChannelDescriptor channelDescriptor) {
        // TODO Auto-generated method stub

    }

}
