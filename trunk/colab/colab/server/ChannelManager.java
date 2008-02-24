package colab.server;

import java.rmi.RemoteException;
import java.util.HashMap;


import colab.common.channel.ChannelName;
import colab.common.community.CommunityName;

/**
 * A channel manager provides channels.  This implementation
 * does not load any channel data into memory until a channel
 * is joined by one or more users.
 *
 */
public class ChannelManager {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /** Map of all channels, indexed by community name, then channel name */
    private HashMap<CommunityName, HashMap<ChannelName, ServerChannel>> channelMap;

    /**
     * Constructs a new channel manager.
     *
     */
    public ChannelManager() {
        channelMap = new HashMap<CommunityName, HashMap<ChannelName, ServerChannel>>();
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

        ServerChannel result = null;

        HashMap<ChannelName, ServerChannel> subMap;

        // Check for community entry
        if (channelMap.containsKey(communityName)) {

            subMap = channelMap.get(communityName);

        } else {

            // The community did not exist in the map, so create an entry
            subMap = new HashMap<ChannelName, ServerChannel>();

            channelMap.put(communityName, subMap);
        }

        // Check for channel entry
        if (subMap.containsKey(channelName)) {
            result = subMap.get(channelName);

        } else {

            // Channel not found, create and add to map
            result = createChannel(channelName);
            subMap.put(channelName, result);

        }

        return result;

    }


    // TODO: This method needs to know the type of channel to create. Right now
    // it just creates a chat channel!
    private final ServerChannel createChannel(final ChannelName channelName) throws RemoteException {
        return new ServerChatChannel(channelName);
    }

}
