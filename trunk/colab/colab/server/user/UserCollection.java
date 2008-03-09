package colab.server.user;

import colab.common.identity.IdentitySet;
import colab.common.naming.UserName;

public final class UserCollection implements UserStore {

    /**
     * All of the users that exist.
     */
    private final IdentitySet<UserName, User> users;

    public UserCollection() {
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
