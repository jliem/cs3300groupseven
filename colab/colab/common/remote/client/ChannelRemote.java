package colab.common.remote.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import colab.common.channel.ChannelData;
import colab.common.naming.UserName;

/**
 * A remote interface for a client-side object which the
 * server application uses when it needs to update the client
 * with some information about a channel in which the client
 * is participating.
 */
public interface ChannelRemote extends Remote {

    /**
     * Informs the client that data has been added to the channel.
     *
     * @param data the piece of data
     * @throws RemoteException if an rmi error occurs
     */
    void add(ChannelData data) throws RemoteException;

    /**
     * Informs the client that a user has joined (been added to) the channel.
     *
     * @param userName the name of the user
     * @throws RemoteException if an rmi error occurs
     */
    void userJoined(UserName userName) throws RemoteException;

    /**
     * Informs the client that a user has left (been removed from) the channel.
     *
     * @param userName the name of a user
     * @throws RemoteException if an rmi error occurs
     */
    void userLeft(UserName userName) throws RemoteException;

}
