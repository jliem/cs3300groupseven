package colab.server;

import java.util.Collection;

import colab.common.identity.IdentitySet;
import colab.common.naming.CommunityName;

public class CommunityCollection implements CommunityStore {

    /**
     * All of the communities that exist.
     */
    private final IdentitySet<CommunityName, Community> communities;

    public CommunityCollection() {
        this.communities = new IdentitySet<CommunityName, Community>();
    }

    public void add(final Community community) {
        communities.add(community);
    }

    public Community get(final CommunityName name) {
        return communities.get(name);
    }

    public Collection<Community> getAll() {
        return communities;
    }

}
