package colab.server;

import java.rmi.RMISecurityManager;

import colab.community.CommunityManager;

public class ColabServer {

	private final CommunityManager communityManager;
	
	public ColabServer() {
		communityManager = new CommunityManager();
	}
	
	public static void main(String[] args) {
		
		// Assign a security manager, in the event that dynamic classes are loaded.
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}
		
	}
	
}
