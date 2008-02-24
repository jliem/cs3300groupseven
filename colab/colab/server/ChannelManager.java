package colab.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
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
     * Returns a Collection of a Community's channels.
     *
     * @param communityName the Community to look up
     * @return a non-null Collection of its channels. If there are no channels, the resulting
     * Collection will have size()==0.
     */
    public final Collection<ServerChannel> getChannelsByCommunity(final CommunityName communityName) {

        Collection<ServerChannel> result = null;

        if (channelMap.containsKey(communityName)) {
            // The community is in the map, so find what channels it has
            HashMap<ChannelName, ServerChannel> subMap = channelMap.get(communityName);

            // Does this return null if there are no values, or just an empty list?
            result = subMap.values();
        }

        if (result == null) {
            // There are no channels, so return an empty list
            result = new ArrayList<ServerChannel>();
        }

        return result;
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
