package colab.server;

import java.util.Collection;

import colab.common.community.CommunityName;
import colab.common.identity.IdentitySet;
import colab.common.user.UserName;

/**
 * A simple user manager that holds all users and communities in memory.
 */
class UserManager {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * All of the communities that exist on a server.
     */
    private final IdentitySet<CommunityName, Community> communities;

    /**
     * All of the users that exist on a server.
     */
    private final IdentitySet<UserName, User> users;

    /**
     * Constructs an "empty" user manager with no communities or users.
     */
    public UserManager() {

        // Create an empty set of communities.
        communities = new IdentitySet<CommunityName, Community>();

        // Create an empty set of users.
        users = new IdentitySet<UserName, User>();

    }

    /**
     * Retrieves a community.
     *
     * @param name the name of the community
     * @return the community with the given name
     */
    public final Community getCommunity(final CommunityName name) {
        return communities.get(name);
    }

    /**
     * Retrieves all of the communities on the server.
     *
     * @return a collection containing every community
     */
    public final Collection<Community> getAllCommunities() {
        return communities;
    }

    /**
     * Adds a new community.
     *
     * @param community the new community to add
     */
    public final void addCommunity(final Community community) {
        communities.add(community);
    }

    /**
     * Retrieves a user.
     *
     * @param name the name of the user to retrieve
     * @return the user with the given name
     */
    public final User getUser(final UserName name) {
        return users.get(name);
    }

    /**
     * Adds a new user.
     *
     * @param user the new user to add
     */
    public final void addUser(final User user) {
        users.add(user);
    }

}
