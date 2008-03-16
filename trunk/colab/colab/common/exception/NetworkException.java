package colab.common.exception;

import java.rmi.RemoteException;

/**
 * A general superclass for exceptions which indicate network failure.
 */
public class NetworkException extends RemoteException {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs a new NetworkException.
     */
    public NetworkException() {
        super();
    }

    /**
     * Constructs a new NetworkException with the specified cause.
     *
     * @param cause the cause
     */
    public NetworkException(final Throwable cause) {
        super("Network error", cause);
    }

}
