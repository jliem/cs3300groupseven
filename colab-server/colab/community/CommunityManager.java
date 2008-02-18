package colab.community;

import colab.identity.IdentitySet;

/**
 * A simple community manager implementation that holds all
 * communities in memory.
 */
public class CommunityManager implements CommunityManagerInterface {

	private final IdentitySet<CommunityName, Community> communities;

	public CommunityManager() {

		// Create an empty set of communities.
		communities = new IdentitySet<CommunityName, Community>();
		
	}

	public Community get(final CommunityName name) {
		return communities.get(name);
	}

	public void add(final Community community) {
		communities.add(community);
	}

}
