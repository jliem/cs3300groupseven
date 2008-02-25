package colab.common.exception;

import java.rmi.RemoteException;

public class NetworkException extends RemoteException {

    public NetworkException() {
        super();
    }

    public NetworkException(final Throwable cause) {
        super("Network error", cause);
    }

}
