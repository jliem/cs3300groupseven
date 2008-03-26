package colab.server.user;

import colab.common.naming.UserName;

/**
 * A collection of users.
 */
public interface UserStore {

    /**
     * Adds a user to the collection.
     *
     * @param user a user to add
     */
    void add(User user);

    /**
     * Retrieves a user by username.
     *
     * @param name the name of the desired user
     * @return the user with the given name, or
     *         null if no such user exists
     */
    User get(UserName name);

}
