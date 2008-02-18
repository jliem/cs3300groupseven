package colab.server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collection;

import colab.community.Community;
import colab.community.CommunityName;
import colab.identity.IdentitySet;

/**
 * A simple community manager implementation that holds all
 * communities in memory.
 */
public class CommunityManager implements CommunityManagerInterface, Serializable {

	public static final long serialVersionUID = 1L;
	
	private final IdentitySet<CommunityName, Community> communities;

	public CommunityManager() {

		// Create an empty set of communities.
		communities = new IdentitySet<CommunityName, Community>();
		
	}
	
	public Community get(final CommunityName name) throws RemoteException {
		return communities.get(name);
	}

	public Collection<Community> getAll() throws RemoteException {
		return communities;
	}
	
	public void add(final Community community) {
		communities.add(community);
	}

}
