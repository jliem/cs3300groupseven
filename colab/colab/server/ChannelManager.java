package colab.server;

import java.rmi.RemoteException;

import colab.channel.ChannelName;
import colab.community.CommunityName;

/**
 * A channel manager provides channels.  This implementation
 * does not load any channel data into memory until a channel
 * is joined by one or more users.
 */
public class ChannelManager {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs a new channel manager.
     *
     * @throws RemoteException if an rmi error occurs
     */
    public ChannelManager() throws RemoteException {

    }

    /**
     * Retrieves a channel.
     * The channel will be created if it does not exist.
     *
     * @param communityName the name of the community to which
     *                      the channel belongs
     * @param channelName the name of the channel requested
     * @return a remote reference to the requested channel
     * @throws RemoteException if an rmi error occurs
     */
    public final ServerChannel getChannel(final CommunityName communityName,
            final ChannelName channelName) throws RemoteException {

        // TODO Auto-generated method stub
        return null;

    }

}
