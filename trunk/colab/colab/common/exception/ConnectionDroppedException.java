package colab.common.exception;

/**
 * Indicates that the connection has been lost entirely.
 */
public class ConnectionDroppedException extends NetworkException {

    public ConnectionDroppedException() {
        super();
    }

    public ConnectionDroppedException(final Throwable cause) {
        super(cause);
    }

}
