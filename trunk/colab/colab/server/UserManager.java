package colab.server;

import java.util.Collection;

import colab.common.identity.IdentitySet;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import colab.common.remote.exception.AuthenticationException;
import colab.common.remote.exception.IncorrectPasswordException;
import colab.common.remote.exception.UserDoesNotExistException;

/**
 * A simple user manager that holds all users and communities in memory.
 */
public final class UserManager {

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

    private ColabServer server;

    /**
     * Constructs an "empty" user manager with no communities or users.
     */
    public UserManager(final ColabServer server) {

        this.server = server;

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
    public Community getCommunity(final CommunityName name) {
        return communities.get(name);
    }

    /**
     * Retrieves all of the communities on the server.
     *
     * @return a collection containing every community
     */
    public Collection<Community> getAllCommunities() {
        return communities;
    }

    /**
     * Adds a new community.
     *
     * @param community the new community to add
     */
    public void addCommunity(final Community community) {
        communities.add(community);
    }

    /**
     * Retrieves a user.
     *
     * @param name the name of the user to retrieve
     * @return the user with the given name
     */
    public User getUser(final UserName name) {
        return users.get(name);
    }

    /**
     * Adds a new user.
     *
     * @param user the new user to add
     */
    public void addUser(final User user) {
        users.add(user);
    }

    public void checkPassword(final UserName username,
            final char[] password) throws AuthenticationException {

        User userAttempt = getUser(username);

        if (userAttempt == null) {
            throw new UserDoesNotExistException();
        }

        if (password == null || !userAttempt.checkPassword(password)) {
            throw new IncorrectPasswordException();
        }

    }

}
