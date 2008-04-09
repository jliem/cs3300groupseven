package colab.server;

import junit.framework.TestCase;
import colab.client.ColabClient;
import colab.common.remote.server.ConnectionRemote;

/**
 * Test cases for {@link ColabServer}. 
 */
public final class ColabServerTester extends TestCase {

    /**
     * Makes sure that a server connection is made and is not null
     * @throws Exception
     */
	public void testCreateServerandConnect() throws Exception {

        ColabServer server = new MockColabServer();
        ColabClient client = new ColabClient();

        ConnectionRemote connection = server.connect(client);

        assertNotNull(connection);

    }

}
