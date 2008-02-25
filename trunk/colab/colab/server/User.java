package colab.server;

import java.io.Serializable;

import colab.common.identity.Identifiable;
import colab.common.naming.UserName;

/**
 * Represents a user of the system.
 */
class User implements Identifiable<UserName>, Serializable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * A unique string identifying this user.
     */
    private final UserName name;

    /**
     * The password for this user to log in.
     */
    private Password pass;

    /**
     * Constructs a new User.
     *
     * @param name a unique string identifying this user
     * @param pass the password for this user to log in
     */
    public User(final UserName name, final Password pass) {
        this.name = name;
        this.pass = pass;
    }

    /**
     * Constructs a new User.
     *
     * @param name a unique string identifying this user
     * @param pass the password for this user to log in
     */
    public User(final String name, final String pass) {
        this(new UserName(name), new Password(pass));
    }

    /**
     * Sets the user's password.
     *
     * @param pass the new password for this user
     */
    public final void setPassword(final Password pass) {
        this.pass = pass;
    }

    /**
     * Verifies whether a given password string is correct for this user.
     *
     * @param attempt an input string which may be a correct password
     * @return true if the given password is correct, false otherwise
     */
    public final boolean checkPassword(final char[] attempt) {
        return pass.checkPassword(attempt);
    }

    /**
     * Returns the string which identifies this user.
     *
     * @return the name of this user
     */
    public final UserName getId() {
        return name;
    }

    public String toString() {
        return getId().toString();
    }
}
