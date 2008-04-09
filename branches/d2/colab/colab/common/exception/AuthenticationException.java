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

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public AuthenticationException(final String message) {
        super(message);
    }
}
