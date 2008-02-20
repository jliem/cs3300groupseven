package colab.client;

import java.rmi.Naming;

import colab.community.CommunityName;
import colab.server.ColabServerInterface;
import colab.server.ConnectionInterface;
import colab.user.UserName;

/**
 * The CoLab client application.
 */
public final class ColabClient {

    /** The default port. */
    public static final int DEFAULT_PORT = 9040;

    /**
     * Constructs the client application.
     */
    private ColabClient() {

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
            } else {
                System.out.println("Community login failed.");
            }
        } else {
            System.out.println("Login failed.");
        }

    }

}
