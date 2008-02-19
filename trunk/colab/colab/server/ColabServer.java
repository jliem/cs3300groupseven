package colab.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import colab.community.Community;
import colab.user.User;

public class ColabServer extends UnicastRemoteObject implements ColabServerInterface {

	public static final long serialVersionUID = 1L;
	
	private final UserManager userManager;

	public ColabServer() throws RemoteException {

		// Create the manager objects
		this.userManager = new UserManager();
//		System.out.println(this.userManager);
		
	}
	
	public ConnectionInterface connect() throws RemoteException {
		return new Connection(this);
	}

	public UserManagerInterface getUserManager() throws RemoteException {
		//System.out.println(this.userManager);
		return this.userManager;
	}

	public static void main(String[] args) throws Exception {
		
		// Assign a security manager, in the event
		// that dynamic classes are loaded
		//if (System.getSecurityManager() == null) {
		//	System.setSecurityManager(new RMISecurityManager());
		//}

		int port = 9040;
		
		// Create a server
		ColabServer server = new ColabServer();
		
		// Create the rmi registry, add the server to it
		LocateRegistry.createRegistry(port);
		Naming.rebind("//localhost:" + port + "/COLAB_SERVER", server);

		// Populate a few test communities
		server.userManager.addCommunity(new Community("Group Seven", "sevenPass"));
		server.userManager.addCommunity(new Community("Team Awesome", "awesomePass"));
		
		// Populate a few test users
		server.userManager.addUser(new User("Johannes", "pass1"));
		server.userManager.addUser(new User("Pamela", "pass2"));
		server.userManager.addUser(new User("Matthew", "pass3"));
		server.userManager.addUser(new User("Chris", "pass4"));

		System.out.println("Server initialized");
	
	}
	
}
