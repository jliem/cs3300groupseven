package colab.user;

import colab.identity.StringIdentifier;

/**
 * The name a user uses to log in.
 */
public class UserName extends StringIdentifier {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs a new user name.
     *
     * @param name the name of a user
     */
    public UserName(final String name) {
        super(name);
    }

}
