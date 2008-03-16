package colab.common.exception;

/**
 * Indicates that a connection has been lost entirely.
 */
public class ConnectionDroppedException extends NetworkException {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs a new ConnectionDroppedException.
     */
    public ConnectionDroppedException() {
        super();
    }

    /**
     * Constructs a new ConnectionDroppedException with the specified cause.
     *
     * @param cause the cause
     */
    public ConnectionDroppedException(final Throwable cause) {
        super(cause);
    }

}
