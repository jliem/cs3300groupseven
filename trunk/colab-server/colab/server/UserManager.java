package colab.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;

import colab.community.Community;
import colab.community.CommunityName;
import colab.identity.IdentitySet;
import colab.user.User;
import colab.user.UserName;

/**
 * A simple user manager implementation that holds all
 * users and communities in memory.
 */
public class UserManager extends UnicastRemoteObject
		implements UserManagerInterface {

	public static final long serialVersionUID = 1L;
	
	private final IdentitySet<CommunityName, Community> communities;
	private final IdentitySet<UserName, User> users;

	public UserManager() throws RemoteException {

		// Create an empty set of communities.
		communities = new IdentitySet<CommunityName, Community>();
		
		// Create an empty set of users.
		users = new IdentitySet<UserName, User>();
		
	}
	
	public Community getCommunity(final CommunityName name) throws RemoteException {
		return communities.get(name);
	}

	public Collection<Community> getAllCommunities() throws RemoteException {
		return communities;
	}
	
	public boolean addCommunity(final Community community) {
		return communities.add(community);
	}

	public User getUser(final UserName name) {
		return users.get(name);
	}
	
	public boolean addUser(final User user) {
		return users.add(user);
	}
	
}
