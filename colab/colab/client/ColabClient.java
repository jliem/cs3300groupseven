package colab.client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import colab.client.remote.ColabClientInterface;
import colab.common.exception.AuthenticationException;
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
     * @return whether the client connected successfully
     */
    public boolean connect(final String serverAddress) {

        ConnectionInterface connection;
        try {
            ColabServerInterface server = (ColabServerInterface) Naming.lookup(
                    "//" + serverAddress + "/COLAB_SERVER");
            connection = server.connect(this);
        } catch (final Exception e) {
            return false;
        }

        this.connection = connection;

        return true;

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
     * @return whether or not the user is logged in
     */

    public boolean loginUser(final String username, final char[] password,
            final String serverAddress) {
/*
        if (!connect(serverAddress)) {
            return false;
        }

        try {
            this.connection.logIn(userName, password);
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (RemoteException re) {
            re.printStackTrace();
            return false;
        }
*/
        return true;

    }

}
