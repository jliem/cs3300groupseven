package colab.common.exception;

/**
 * Thrown when a user, already logged into a community, attempts
 * to perform an additional concurrent login on a different connection.
 */
public class UserAlreadyLoggedInException extends AuthenticationException {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

}
