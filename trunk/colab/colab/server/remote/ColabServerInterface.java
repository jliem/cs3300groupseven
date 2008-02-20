package colab.server.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * The server interface is the first point of contact that the client
 * has to the server.  A single instance should be registered with the
 * rmi registry so that clients can make invocations upon it.
 *
 * The purpose of this remote interface is for the client to gain a remote
 * reference to a {@link ConnectionInterface} for any further activity.
 */
public interface ColabServerInterface extends Remote {

    /**
     * Creates a connection object for the client.
     *
     * @return a new remote object dedicated to handling
     *         invocations from the client who requested it
     * @throws RemoteException if an rmi error occurs
     */
    ConnectionInterface connect() throws RemoteException;

}
