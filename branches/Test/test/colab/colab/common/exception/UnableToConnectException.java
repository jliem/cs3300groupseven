package colab.common.exception;

/**
 * Indicates that an attempt to initialize a connection with a
 * remote service failed.
 */
public class UnableToConnectException extends NetworkException {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs a new UnableToConnectException.
     */
    public UnableToConnectException() {
        super();
    }

    /**
     * Constructs a new UnableToConnectException with the specified cause.
     *
     * @param cause the cause
     */
    public UnableToConnectException(final Throwable cause) {
        super(cause);
    }

}
