package colab.common.remote.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import colab.common.channel.ChannelDescriptor;

/**
 * A remote interface for a client-side object which the
 * server application uses when it needs to update the client
 * with some information that is not specific to any
 * particular channel.
 */
public interface ColabClientInterface extends Remote {

    /**
     * Notifies the client that a channel exists in the community
     * in which the user is logged in.
     *
     * @param channelDescriptor the channel
     * @throws RemoteException if an rmi error occurs
     */
    void channelAdded(ChannelDescriptor channelDescriptor)
            throws RemoteException;

}
