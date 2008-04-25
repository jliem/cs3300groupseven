package colab.server.connection;

import junit.framework.TestCase;
import colab.client.ColabClient;
import colab.common.ConnectionState;
import colab.server.MockColabServer;

/**
 * Test cases for {@link Connection}.
 */
public final class ConnectionTest extends TestCase {

    /**
     * Goes through the possible connection states to make sure
     * that the Connection maintains its state properly.
     *
     * @throws Exception if any exception is thrown
     */
    public void testConnectionCreationandCheckState() throws Exception {

        ColabClient client = new ColabClient();
        MockColabServer server = new MockColabServer();
        Connection comm = new Connection(server, client);

        assertSame(comm.getState(), ConnectionState.CONNECTED);

    }

}
