package colab.server;

import junit.framework.TestCase;
import colab.server.ColabServer;
import colab.client.ColabClient;
import colab.common.remote.server.ConnectionInterface;

public class ColabServerTester extends TestCase {
	
	public void testCreateServerandConnect() throws Exception {
	
		ColabServer server = new MockColabServer();
		ColabClient client = new ColabClient();
		
		ConnectionInterface connection = server.connect(client);
		
		assertNotNull(connection);
		
	}

}
