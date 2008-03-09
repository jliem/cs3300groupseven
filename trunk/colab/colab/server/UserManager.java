package colab.server;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import colab.common.exception.AuthenticationException;
import colab.common.exception.CommunityDoesNotExistException;
import colab.common.exception.IncorrectPasswordException;
import colab.common.exception.UserAlreadyExistsException;
import colab.common.exception.UserDoesNotExistException;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import colab.common.util.FileUtils;
import colab.server.file.CommunityFile;
import colab.server.file.UserFile;

/**
 * A simple user manager that holds all users and communities in memory.
 */
public final class UserManager {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private final ColabServer server;

    private final UserStore userStore;

    private final CommunityStore communityStore;

    public UserManager(final ColabServer server) {

        this.server = server;
        this.userStore = new UserCollection();
        this.communityStore = new CommunityCollection();

    }

    public UserManager(final ColabServer server, final File directory)
            throws IOException {

        this.server = server;
        this.userStore = new UserFile(
                FileUtils.getFile(directory, "users"));
        this.communityStore = new CommunityFile(
                FileUtils.getFile(directory, "communities"));

    }

    /**
     * Retrieves a community.
     *
     * @param name the name of the community
     * @return the community with the given name
     */
    public Community getCommunity(final CommunityName name)
            throws CommunityDoesNotExistException {

        Community community = communityStore.get(name);

        if (community == null) {
            throw new CommunityDoesNotExistException();
        }

        return community;

    }

    /**
     * Retrieves all of the communities on the server.
     *
     * @return a collection containing every community
     */
    public Collection<Community> getAllCommunities() {
        return communityStore.getAll();
    }

    /**
     * Adds a new community.
     *
     * @param community the new community to add
     */
    public void addCommunity(final Community community) {
        communityStore.add(community);
    }

    /**
     * Retrieves a user.
     *
     * @param name the name of the user to retrieve
     * @return the user with the given name
     */
    public User getUser(final UserName name)
            throws UserDoesNotExistException {

        User user = userStore.get(name);

        if (user == null) {
            throw new UserDoesNotExistException();
        }

        return user;

    }

    /**
     * Adds a new user.
     *
     * @param user the new user to add
     */
    public void addUser(final User user)
            throws UserAlreadyExistsException {

        if (userStore.get(user.getId()) != null) {
            throw new UserAlreadyExistsException();
        }

        userStore.add(user);

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
