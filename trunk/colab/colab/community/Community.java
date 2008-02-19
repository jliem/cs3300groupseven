package colab.community;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import colab.identity.Identifiable;
import colab.user.Password;
import colab.user.User;

/**
 * Represents a community which can be joined by users.
 */
public class Community implements Identifiable<CommunityName>, Serializable {

	public static final long serialVersionUID = 1L;

	/**
	 * A unique string identifying this community.
	 */
	private final CommunityName name;
	
	/**
	 * The users which have joined this community and can log in to it.
	 */
	private final Collection<User> members;
	
	/**
	 * The password to join this community.
	 */
	private Password password;
	
	public Community(final CommunityName name, final Password password) {

		// Set the community name
		this.name = name;
		
		// Set the community password
		this.password = password;

		// Create an empty collection of users
		members = new ArrayList<User>();

	}

	/**
	 * Constructs a new Community.
	 * 
	 * @param name a unique string identifying this community
	 * @param pass the password to join this community
	 */
	public Community(final String name, final String password) {
		this(new CommunityName(name), new Password(password));
	}
	
	/**
	 * Returns the string which identifies this community.
	 * 
	 * @return the name of this community
	 */
	public CommunityName getId() {
		return name;
	}
	
	/**
	 * Returns the users which are members of this community.
	 * 
	 * @return a collection containing every user of this community
	 */
	public Collection<User> getMembers() {
		return members;
	}

	/**
	 * Verifies whether a given password string is correct for this community.
	 * 
	 * @param attempt an input string which may be a correct password
	 * @return true if the given password is correct, false otherwise
	 */
	public boolean checkPassword(final String attempt) {
		return password.checkPassword(attempt);
	}

	public boolean authenticate(final User user, final String passAttempt) {
		
		// If user is a member of the group, authentication is successful.
		if (members.contains(user)) {
			return true;
		}
	
		// If a correct password was provided, the user can join.
		if (password != null && checkPassword(passAttempt)) {
			members.add(user);
			return true;
		}

		// If neither, the user cannot authenticate to this community.
		return false;
		
	}
	
}
