package colab.server.user;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import colab.common.exception.AuthenticationException;
import colab.common.exception.CommunityAlreadyExistsException;
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

    /** The collection of users. */
    private final UserStore userStore;

    /** The collection of communities. */
    private final CommunityStore communityStore;

    /**
     * Constructs a new UserManager which does not use persistent storage.
     */
    public UserManager() {

        this.userStore = new UserSet();
        this.communityStore = new CommunitySet();

    }

    /**
     * Constructs a new UserManager which uses a file for persistent storage.
     *
     * @param directory the directory to use for storage
     * @throws IOException if an I/O exception occurs
     */
    public UserManager(final File directory) throws IOException {

        File userFile = FileUtils.getFile(directory, "users");
        this.userStore = new UserFile(userFile);

        File communityFile = FileUtils.getFile(directory, "communities");
        this.communityStore = new CommunityFile(communityFile);

    }

    /**
     * Retrieves a community.
     *
     * @param name the name of the community
     * @return the community with the given name
     * @throws CommunityDoesNotExistException if the community does not exist
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

        System.out.println("Communities:");

        for (Community c : communityStore.getAll()) {
            System.out.println(c.getId());
        }

        return communityStore.getAll();
    }

    /**
     * Adds a new community.
     *
     * @param community the new community to add
     * @throws CommunityAlreadyExistsException if the community already exists
     */
    public void addCommunity(final Community community)
            throws CommunityAlreadyExistsException {

        if (communityStore.get(community.getId()) != null) {
            throw new CommunityAlreadyExistsException();
        }

        communityStore.add(community);

        System.out.println("UserManager added community " + community.getId() + community);

    }

    /**
     * Retrieves a user.
     *
     * @param name the name of the user to retrieve
     * @return the user with the given name
     * @throws UserDoesNotExistException if no user with the given name exists
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
     * @throws UserAlreadyExistsException if a user with the given
     *                                    name already exists
     */
    public void addUser(final User user)
            throws UserAlreadyExistsException {

        if (userStore.get(user.getId()) != null) {
            throw new UserAlreadyExistsException();
        }

        userStore.add(user);

    }

    /**
     * Checks that a user's password is correct.
     *
     * @param username a username
     * @param password a password
     * @throws AuthenticationException if the password is incorrect
     *                                 for the user
     */
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
