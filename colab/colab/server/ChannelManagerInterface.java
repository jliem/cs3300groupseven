package colab.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import colab.channel.Channel;

/**
 * A remote class based in the server application.
 * Once a client has authenticated and joined a community, a
 * {@link ChannelManagerInterface} is created for that session,
 * to provide {@link Channel} objects when the user joins a channel.
 */
public interface ChannelManagerInterface extends Remote {

    /**
     * Retrieves a channel.
     * The channel will be created if it does not exist.
     *
     * @param channelName the name of the channel requested
     * @return a remote reference to the requested channel
     * @throws RemoteException if an rmi error occurs
     */
    Channel getChannel(String channelName) throws RemoteException;

}
