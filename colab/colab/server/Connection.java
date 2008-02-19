package colab.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import colab.community.Community;
import colab.community.CommunityName;
import colab.user.User;
import colab.user.UserName;

public class Connection extends UnicastRemoteObject implements ConnectionInterface {
	
	public static final long serialVersionUID = 1L;
	
	public enum STATE {
		
		/**
		 * Client has a connection to the server,
		 * but no authentication has taken place.
		 */
		CONNECTED,
		
		/**
		 * User has been authenticated, but no
		 * community has been joined.
		 */
		LOGGED_IN,
		
		/**
		 * The user is logged into a community and
		 * can actively use the system.
		 */
		ACTIVE
		
	}
	
	private STATE state;
	
	private final ColabServer server;
	
	private User user;
	private Community community;
	
	public Connection(final ColabServer server) throws RemoteException {
		
		this.server = server;
		
		state = STATE.CONNECTED;
		
	}
	
	public User getUser() throws RemoteException {
		return this.user;
	}
	
	public STATE getState() {
		return this.state;
	}
	
	/** {@inheritDoc} */
	public boolean logIn(final UserName username, final String password)
			throws RemoteException {
		
		UserManagerInterface userManager = server.getUserManager();
		User userAttempt = userManager.getUser(username);
		boolean correct = userAttempt.checkPassword(password);

		if (correct) {
			this.user = userAttempt;
			this.state = STATE.LOGGED_IN;
		}
		
		return correct;
		
	}
	
	/** {@inheritDoc} */
	public boolean logIn(final CommunityName communityName, final String password)
			throws RemoteException {
		
		UserManagerInterface userManager = server.getUserManager();
		Community communityAttempt = userManager.getCommunity(communityName);
		boolean correct = communityAttempt.getMembers().contains(this.user)
			| communityAttempt.checkPassword(password);
		
		if (correct) {
			this.community = communityAttempt;
			this.state = STATE.ACTIVE;
		}
		
		return correct;
			
	}
	
}
