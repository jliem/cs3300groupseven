package colab.server.connection;

import junit.framework.TestCase;
import colab.client.ColabClient;
import colab.common.ConnectionState;
import colab.server.MockColabServer;

/**
 * 
 * Test case for ConnectionTest
 *
 */
public final class ConnectionTest extends TestCase {

    /**
     * Tests that a Connection is made from the client to a server and 
     * that the connection returns the right state
     * @throws Exception
     */
	
	public void testConnectionCreationandCheckState() throws Exception {

        ColabClient client = new ColabClient();
        MockColabServer server = new MockColabServer();
        Connection comm = new Connection(server, client);

        assertSame(comm.getState(), ConnectionState.CONNECTED);

    }

}
