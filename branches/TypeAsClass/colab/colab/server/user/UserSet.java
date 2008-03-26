package colab.server.user;

import colab.common.identity.IdentitySet;
import colab.common.naming.UserName;

/**
 * A simple UserStore which stores all users in a set.
 */
public final class UserSet implements UserStore {

    /**
     * All of the users that exist.
     */
    private final IdentitySet<UserName, User> users;

    /**
     * Constructs an empty UserSet.
     */
    public UserSet() {
        this.users = new IdentitySet<UserName, User>();
    }

    /** {@inheritDoc} */
    public void add(final User user) {
        users.add(user);
    }

    /** {@inheritDoc} */
    public User get(final UserName name) {
        return users.get(name);
    }

}
