package colab.server;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import colab.community.Community;

public class ColabServer implements ServerInterface, Serializable {

	public static final long serialVersionUID = 1L;
	
	private final CommunityManager communityManager;

	public ColabServer() {
		communityManager = new CommunityManager();
	}
	
	public Connection connect() throws RemoteException {
		return new Connection();
	}
	
	public static void main(String[] args) {
		
		// Assign a security manager, in the event
		// that dynamic classes are loaded
		if (System.getSecurityManager() == null) {
			//System.setSecurityManager(new RMISecurityManager());
			System.setSecurityManager(null);
		}
		
		// Create a server
		ColabServer server = new ColabServer();

		final int port = 9040;
		
		// Create the rmi registry, add the server to it
		try {
			LocateRegistry.createRegistry(port);
			Naming.rebind("//localhost:" + port + "/COLAB_SERVER", server);
		} catch (MalformedURLException me) {
			me.printStackTrace();
		} catch (final RemoteException re) {
			re.printStackTrace();
		}
		
		// For test purposes, populate a few communities
		server.communityManager.add(new Community("Group Seven"));
		server.communityManager.add(new Community("Team Awesome"));
		
	}
	
}
