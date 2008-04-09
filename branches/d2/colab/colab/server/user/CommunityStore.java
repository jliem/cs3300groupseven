package colab.server.user;

import java.util.Collection;

import colab.common.naming.CommunityName;

/**
 * A collection of communities.
 */
public interface CommunityStore {

    /**
     * Retrieves the entire community collection.
     *
     * @return all of the communities
     */
    Collection<Community> getAll();

    /**
     * Adds a new community.
     *
     * @param community a community to add
     */
    void add(Community community);

    /**
     * Retrieves a community by name.
     *
     * @param name the name of the desired community
     * @return the community with the given name, or
     *         null if no such community exists
     */
    Community get(CommunityName name);

}
