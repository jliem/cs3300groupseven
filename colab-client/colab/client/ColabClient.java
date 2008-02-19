package colab.client;

import java.rmi.Naming;

import colab.server.ConnectionInterface;
import colab.server.ServerInterface;
import colab.user.UserName;

public class ColabClient {

	public ColabClient() {
		
	}
	
	public static void main(String[] args) throws Exception {

		// Assign a security manager, in the event that dynamic classes are loaded.
		//if (System.getSecurityManager() == null) {
		//	System.setSecurityManager(new RMISecurityManager());
		//}
		
		int port = 9040;
		
        String url = "//localhost:" + port + "/COLAB_SERVER";
        
		ServerInterface server = (ServerInterface) Naming.lookup(url);
		ConnectionInterface connection = server.connect();
		boolean correct = connection.logIn(new UserName("Chris"), "pass4");
		if (correct) {
			System.out.println("User logged in.");
		} else {
			System.out.println("Login failed.");
		}
		
	}
	
}
