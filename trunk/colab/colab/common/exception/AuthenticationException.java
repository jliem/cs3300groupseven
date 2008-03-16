package colab.common.exception;

/**
 * Indicates a failure to present valid login credentials.
 */
public class AuthenticationException extends Exception {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs a new AuthenticationException.
     */
    public AuthenticationException() {
        super();
    }

    /**
     * Constructs a new AuthenticationException with the specified cause.
     *
     * @param cause the cause
     */
    public AuthenticationException(final Throwable cause) {
        super(cause);
    }
}
