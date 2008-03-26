package colab.common.exception;

/**
 * Indicates an attempt to create a new user account when
 * a user with the provided name already exists.
 */
public class UserAlreadyExistsException extends Exception {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

}
