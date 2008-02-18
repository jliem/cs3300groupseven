package colab.server;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import colab.community.Community;
import colab.user.User;

public class ColabServer implements ServerInterface, Serializable {

	public static final long serialVersionUID = 1L;
	
	private final UserManager userManager;

	public ColabServer() {
		userManager = new UserManager();
	}
	
	public Connection connect() throws RemoteException {
		return new Connection(this);
	}

	public UserManager getUserManager() {
		return userManager;
	}
	
	public static void main(String[] args) {
		
		// Assign a security manager, in the event
		// that dynamic classes are loaded
		//if (System.getSecurityManager() == null) {
		//	System.setSecurityManager(new RMISecurityManager());
		//}
		
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
		
		// Populate a few test communities
		server.userManager.addCommunity(new Community("Group Seven"));
		server.userManager.addCommunity(new Community("Team Awesome"));
		
		// Populate a few test users
		server.userManager.addUser(new User("Johannes", "pass1"));
		server.userManager.addUser(new User("Pamela", "pass2"));
		server.userManager.addUser(new User("Matthew", "pass3"));
		server.userManager.addUser(new User("Chris", "pass4"));

	}
	
}
