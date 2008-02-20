package colab.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import colab.channel.Channel;

/**
 * Server implementation of ChannelManagerInterface.
 */
public class ChannelManager extends UnicastRemoteObject
        implements ChannelManagerInterface {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs a new channel manager.
     *
     * @throws RemoteException if an rmi error occurs
     */
    public ChannelManager() throws RemoteException {

    }

    /** {@inheritDoc} */
    public final Channel getChannel(final String channelName)
            throws RemoteException {

        // TODO Auto-generated method stub
        return null;

    }

}
