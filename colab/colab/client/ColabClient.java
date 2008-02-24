package colab.client;

import java.rmi.Naming;

import colab.common.channel.ChannelName;
import colab.common.channel.remote.ChannelInterface;
import colab.common.community.CommunityName;
import colab.common.user.Password;
import colab.common.user.UserName;
import colab.server.remote.ColabServerInterface;
import colab.server.remote.ConnectionInterface;

/**
 * The CoLab client application.
 */
public final class ColabClient {

    /** The default port. */
    public static final int DEFAULT_PORT = 9040;
    private String username; 
    private Password password;

    /**
     * Constructs the client application.
     */
    public ColabClient() {

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
            final String serverIP)
    {
        
        if(connect(serverIP))
        {
            this.username = username;
            this.password = new Password(password);
            //compare to server
            return true;
        }
        return false;
    }

    /**
     * The entry point which launches the client application.
     * @param args unused
     * @throws Exception
     *             if an error occurs in rmi setup
     */
    /*
 public static void main(final String[] args) throws Exception {

        // Assign a security manager, in the event
        // that dynamic classes are loaded.
        //if (System.getSecurityManager() == null) {
        //    System.setSecurityManager(new RMISecurityManager());
        //}

        String url = "//localhost:" + DEFAULT_PORT + "/COLAB_SERVER";

        ColabServerInterface server = (ColabServerInterface) Naming.lookup(url);
        ConnectionInterface connection = server.connect();
        boolean correct = connection.logIn(new UserName("Chris"), "pass4");
        if (correct) {
            System.out.println("User logged in.");
            correct = connection.logIn(
                    new CommunityName("Team Awesome"), "awesomePass");
            if (correct) {
                System.out.println("Logged into community.");
                ChannelName channelName = new ChannelName("Lobby");
                ClientChatChannel clientChannel =
                    new ClientChatChannel(channelName);
                ChannelInterface serverChannel =
                    connection.joinChannel(clientChannel, channelName);
            } else {
                System.out.println("Community login failed.");
            }
        } else {
            System.out.println("Login failed.");
        }

    }
    */

}
