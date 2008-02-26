package colab.server;

import java.rmi.RemoteException;

import colab.client.ColabClient;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import junit.framework.TestCase;
import colab.server.*;
import colab.common.ConnectionState;

public class ConnectionTest extends TestCase {
	
	public void testConnectionCreationandCheckState() throws RemoteException{
		ColabClient client = new ColabClient();
		MockColabServer server = new MockColabServer();
		Connection comm = new Connection(server, client);
		
		assertSame(comm.getState(), ConnectionState.CONNECTED);
			
	}
	
}
