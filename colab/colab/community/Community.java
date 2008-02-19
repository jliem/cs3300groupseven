package colab.community;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import colab.identity.Identifiable;
import colab.user.Password;
import colab.user.User;

public class Community implements Identifiable<CommunityName>, Serializable {

	public static final long serialVersionUID = 1L;
	
	private final CommunityName name;
	private final Collection<User> members;
	private Password password;
	
	public Community(final CommunityName name, final Password password) {

		// Set the community name
		this.name = name;
		
		// Set the community password
		this.password = password;

		// Create an empty collection of users
		members = new ArrayList<User>();

	}

	public Community(final String name, final String password) {
		this(new CommunityName(name), new Password(password));
	}
	
	/**
	 * @return the name of this community
	 */
	public CommunityName getId() {
		return name;
	}
	
	/**
	 * @return a collection containing every user of this community
	 */
	public Collection<User> getMembers() {
		return members;
	}

	public boolean checkPassword(final String attempt) {
		return password.checkPassword(attempt);
	}
	
}
