package colab.client.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import colab.common.channel.ChannelData;

public interface ChannelInterface extends Remote {

    /**
     * Informs the client that data has been added to the channel.
     *
     * @param data the piece of data
     * @throws RemoteException if an rmi error occurs
     */
    void add(ChannelData data)
            throws RemoteException;

}
