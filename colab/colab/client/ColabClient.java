package colab.client;

import java.rmi.Naming;

import colab.common.channel.ChannelName;
import colab.common.community.CommunityName;
import colab.common.user.UserName;
import colab.server.remote.ColabServerInterface;
import colab.server.remote.ConnectionInterface;
import colab.server.remote.ServerChannelInterface;

/**
 * The CoLab client application.
 */
public final class ColabClient {

    /** The default port. */
    public static final int DEFAULT_PORT = 9040;

    /**
     * Constructs the client application.
     */
    public ColabClient() {

    }

    /**
     * The entry point which launches the client application.
     *
     * @param args unused
     * @throws Exception if an error occurs in rmi setup
     */
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
                ServerChannelInterface serverChannel =
                    connection.joinChannel(clientChannel, channelName);
            } else {
                System.out.println("Community login failed.");
            }
        } else {
            System.out.println("Login failed.");
        }

    }

}
