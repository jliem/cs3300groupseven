package colab.common.remote.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import colab.common.remote.client.ColabClientRemote;


/**
 * The server interface is the first point of contact that the client
 * has to the server.  A single instance should be registered with the
 * rmi registry so that clients can make invocations upon it.
 *
 * The purpose of this remote interface is for the client to gain a remote
 * reference to a {@link ConnectionRemote} for any further activity.
 */
public interface ColabServerRemote extends Remote {

    /**
     * Creates a connection object for the client.
     *
     * @param client a remove reference to the client
     *               which is requesting the connection
     * @return a new remote object dedicated to handling
     *         invocations from the client who requested it
     * @throws RemoteException if an rmi error occurs
     */
    ConnectionRemote connect(ColabClientRemote client)
            throws RemoteException;

}
