package colab.community;

import java.util.ArrayList;
import java.util.Collection;

import colab.identifier.Identifiable;
import colab.user.User;

public class Community implements Identifiable<CommunityName> {

	private final CommunityName name;
	private final Collection<User> members;
	
	public Community(final CommunityName name) {
		
		// Set the community name
		this.name = name;
		
		// Create an empty collection of users
		members = new ArrayList<User>();
		
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
	
}
