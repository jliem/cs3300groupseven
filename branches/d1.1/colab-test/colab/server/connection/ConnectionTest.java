package colab.server.connection;

import junit.framework.TestCase;
import colab.client.ColabClient;
import colab.common.ConnectionState;
import colab.server.MockColabServer;

public class ConnectionTest extends TestCase {

    public void testConnectionCreationandCheckState() throws Exception {

        ColabClient client = new ColabClient();
        MockColabServer server = new MockColabServer();
        Connection comm = new Connection(server, client);

        assertSame(comm.getState(), ConnectionState.CONNECTED);

    }

}
