package colab.client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import colab.client.remote.ColabClientInterface;
import colab.common.channel.ChannelName;
import colab.common.community.Community;
import colab.common.community.CommunityName;
import colab.common.user.Password;
import colab.common.user.UserName;
import colab.server.remote.ColabServerInterface;
import colab.server.remote.ConnectionInterface;

/**
 * The CoLab client application.
 */
public final class ColabClient extends UnicastRemoteObject
        implements ColabClientInterface {

    /** The default port. */
    public static final int DEFAULT_PORT = 9040;
    private String username;
    private Password password;

    /**
     * Constructs the client application.
     */
    public ColabClient() throws RemoteException {

    }

    /**
     * Receives the server IP from the GUI and attempts to connect to said
     * server.
     * @param serverIP address
     * @return whether or not the client connected to the server
     */
    public boolean connect(final String serverIP) {

        return true;

    }

    /**
     * Receives the username and password from the GUI fields and checks to
     * see if there is an existing corresponding pair.  If the username
     * exists but the password is incorrect, a ValidationException is thrown.
     * If the username does not exist, then the GUI asks the user if a new
     * user should be added.
     * @param username received from the GUI text field
     * @param password received from the GUI password field
     * @param serverIP address received from the GUI text field
     * @return whether or not the user is logged in
     */

    public boolean loginUser(final String username, final char[] password,
            final String serverIP) {

        if (connect(serverIP)) {
            this.username = username;
            this.password = new Password(password);

            // TODO: compare to server

            return true;
        }

        return false;

    }

}
