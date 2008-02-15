package colab.server;

import java.rmi.RMISecurityManager;

public class ColabServer {

	public static void main(String[] args) {
		
		// Assign a security manager, in the event that dynamic classes are loaded.
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}
		
	}
	
}
