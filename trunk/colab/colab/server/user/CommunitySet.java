package colab.server.user;

import java.util.Collection;

import colab.common.identity.IdentitySet;
import colab.common.naming.CommunityName;

/**
 * A simple CommunityStore which stores all communities in a set.
 */
public class CommunitySet implements CommunityStore {

    /**
     * All of the communities that exist.
     */
    private final IdentitySet<CommunityName, Community> communities;

    /**
     * Constructs an empty CommunitySet.
     */
    public CommunitySet() {
        this.communities = new IdentitySet<CommunityName, Community>();
    }

    /** {@inheritDoc} */
    public final void add(final Community community) {
        communities.add(community);
    }

    /** {@inheritDoc} */
    public final Community get(final CommunityName name) {
        return communities.get(name);
    }

    /** {@inheritDoc} */
    public final Collection<Community> getAll() {
        return communities;
    }

}
