package colab.client;

import junit.framework.TestCase;
import colab.common.exception.UnableToConnectException;
import colab.server.ColabServer;
import colab.server.MockColabServer;

/**
 * Test cases for {@link ColabClient}.
 */
public final class ColabClientTester extends TestCase {

    /** The port on which the server runs. */
    private static final int PORT = 12863;

    /** A server used for testing connectivity. */
    private ColabServer server;

    /**
     * Creates a server and binds it to the RMI registry.
     *
     * @throws Exception if any exception is thrown
     */
    @Override
    public void setUp() throws Exception {

        this.server = new MockColabServer();
        this.server.publish(PORT);

    }

    /**
     * Unbinds the server from the RMI registry.
     *
     * @throws Exception if any exception is thrown
     */
    @Override
    public void tearDown() throws Exception {

        this.server.unpublish();

    }

    /**
     * Tests attempting to connect to an incorrect address.
     *
     * @throws Exception if any unexpected exception is thrown
     */
    public void testConnectFailure() throws Exception {

        ColabClient client = new ColabClient();

        UnableToConnectException expectedException = null;

        try {
            client.connect("bad server");
        } catch (UnableToConnectException e) {
            expectedException = e;
        }

        assertNotNull(expectedException);

    }

    /**
     * Creates a new client and connects to the server.
     *
     * @throws Exception if any exception is thrown
     */
    public void testConnectSuccess() throws Exception {

        ColabClient client = new ColabClient();

        client.connect("localhost:" + PORT);

    }

    /*

        ConnectionRemote connection = server.connect(client);
        connection.logIn(new UserName("Chris"), "pass4".toCharArray());

        Logger.log("User logged in.");

        CommunityName communityName = new CommunityName("Team Awesome");
        connection.logIn(communityName, "awesomePass".toCharArray());

        Logger.log("Logged into community.");

     */

}
