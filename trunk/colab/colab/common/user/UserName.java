package colab.common.user;

import colab.common.exception.naming.InvalidUserNameException;
import colab.common.identity.StringIdentifier;
import colab.naming.ColabNameRules;

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
        if (!ColabNameRules.isValidUserName(name)) {
            throw new InvalidUserNameException();
        }
    }

}
