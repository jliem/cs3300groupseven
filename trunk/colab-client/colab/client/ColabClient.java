package colab.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import colab.server.ConnectionInterface;
import colab.server.ServerInterface;
import colab.user.Password;
import colab.user.UserName;

public class ColabClient {

	public ColabClient() {
		
	}
	
	public static void main(String[] args) {

		// Assign a security manager, in the event that dynamic classes are loaded.
		//if (System.getSecurityManager() == null) {
		//	System.setSecurityManager(new RMISecurityManager());
		//}
		
		int port = 9040;
		
        String url = "//localhost:" + port + "/COLAB_SERVER";
        
        try {
			ServerInterface server = (ServerInterface) Naming.lookup(url);
			ConnectionInterface connection = server.connect();
			boolean correct = connection.logIn(new UserName("Chris"), "Pass4");
			if (correct) {
				System.out.println("Login successful.");
			} else {
				System.out.println("Login failed.");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
	}
	
}
