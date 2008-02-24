package colab.common.exception.remote;

import java.rmi.RemoteException;

public class ChannelDoesNotExistException extends RemoteException {

    public ChannelDoesNotExistException(String s) {
        super(s);
    }
}
