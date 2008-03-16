package colab.server;

import junit.framework.TestCase;
import colab.server.ColabServer;
import colab.client.ColabClient;
import colab.common.remote.server.ConnectionRemote;

public class ColabServerTester extends TestCase {

    public void testCreateServerandConnect() throws Exception {

        ColabServer server = new MockColabServer();
        ColabClient client = new ColabClient();

        ConnectionRemote connection = server.connect(client);

        assertNotNull(connection);

    }

}
