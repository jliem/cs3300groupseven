package colab.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import colab.server.ConnectionInterface;
import colab.server.ServerInterface;

public class ColabClient {

	public ColabClient() {
		
	}
	
	public static void main(String[] args) {

		// Assign a security manager, in the event that dynamic classes are loaded.
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}
		
        String url = "//localhost/STATS-SERVER";
        try {
			ServerInterface server = (ServerInterface) Naming.lookup(url);
			ConnectionInterface connection = server.connect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
	}
	
}
