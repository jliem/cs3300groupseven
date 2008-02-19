package colab.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import colab.community.CommunityName;
import colab.user.UserName;

/**
 * A remote object on the server which represents a client's session.
 */
public interface ConnectionInterface extends Remote {

	/**
	 * Attempts to authenticate a user, using the name and password.
	 * 
	 * @param username the name of the user attempting to log in
	 * @param password the password that the client is attempting to use;
	 *                 may be null, but authentication will fail
	 * @return true if the login was performed successfully, false otherwise
	 * @throws IllegalStateException is the connection is in the wrong state
	 */
	public boolean logIn(UserName username, String password)
		throws RemoteException, IllegalStateException;

	/**
	 * Attempts to log into a community, using the name and optional password.
	 * 
	 * @param username the name of the community to log in to
	 * @param password the password that the client is attempting to use;
	 *                 ignored if the user is already a member of the
	 *                 community, may be null
	 * @return true if the login was performed successfully, false otherwise
	 * @throws IllegalStateException is the connection is in the wrong state
	 */
	public boolean logIn(CommunityName communityName, String password)
		throws RemoteException, IllegalStateException;

	/**
	 * Retrieves the names of every community on the server.
	 * 
	 * @return a collection containing every community name
	 */
	public Collection<CommunityName> getAllCommunityNames()
		throws RemoteException;
	
	/**
	 * Retrieves the names of every community of which the currently
	 * logged-in user is a member.
	 * 
	 * @return a collection containing the name of every community
	 *         in which the user has membership
	 * @throws IllegalStateException if no user is logged in
	 */
	public Collection<CommunityName> getMyCommunityNames()
		throws RemoteException, IllegalStateException;

}
