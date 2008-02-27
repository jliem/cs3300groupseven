package colab.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import colab.common.channel.ChannelDescriptor;
import colab.common.naming.ChannelName;
import colab.common.naming.CommunityName;
import colab.common.remote.exception.ChannelDoesNotExistException;
import colab.common.remote.exception.CommunityDoesNotExistException;

/**
 * A channel manager provides channels.  This implementation
 * does not load any channel data into memory until a channel
 * is joined by one or more users.
 *
 */
class ChannelManager {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /** Map of all channels, indexed by community name, then channel name */
    private HashMap<CommunityName, HashMap<ChannelName, ServerChannel>> channelMap;

    private ColabServer server;

    /**
     * Constructs a new channel manager.
     *
     */
    public ChannelManager(final ColabServer server) {
        this.server = server;

        channelMap = new HashMap<CommunityName, HashMap<ChannelName, ServerChannel>>();
    }

    /**
     * Returns a Collection of a Community's channels.
     *
     * @param communityName the Community to look up
     * @return a non-null Collection of its channels. If there are no channels, the resulting
     * Collection will have size()==0.
     */
    public final Collection<ServerChannel> getChannels(final CommunityName communityName) {

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
     * Adds a new channel or returns the channel if it already existed.
     *
     * @param communityName community name
     * @param channelDescriptor descriptor of the channel to add
     * @return the newly added channel, or the previously existing channel
     *
     * @throws RemoteException if an rmi error occurred
     */
    public ServerChannel addChannel(final CommunityName communityName,
            final ChannelDescriptor channelDescriptor) throws RemoteException {

        // Check whether channel exists
        ChannelName channelName = channelDescriptor.getName();

        if (channelExists(communityName, channelName)) {
            return getChannel(communityName, channelName);
        }

        // Create the new channel
        ServerChannel channel = null;

        switch (channelDescriptor.getType()) {

            case CHAT:
                channel = new ServerChatChannel(channelName);
                break;

            default:
                throw new IllegalArgumentException(
                        "Channel type was unsupported: "
                        + channelDescriptor.getType());
        }

        // Check whether a community entry exists
        HashMap<ChannelName, ServerChannel> subMap = null;

        if (channelMap.containsKey(communityName)) {
            subMap = channelMap.get(communityName);
        } else {

            // No community entry, add it
            subMap = new HashMap<ChannelName, ServerChannel>();

            channelMap.put(communityName, subMap);
        }

        // Make sure channel entry doesn't exist
        if (!subMap.containsKey(channelName)) {
            subMap.put(channelName, channel);
        } else {
            // If the channel entry did exist, something is really wrong
            // since this method already checked whether it exists
            throw new IllegalStateException("channelExists returned false,"
                    + " but the channel already exists!");
        }

        // Because a new channel was added, notify all clients
        UserManager userManager = server.getUserManager();
        Community community = userManager.getCommunity(communityName);
        if (community == null) {
            throw new CommunityDoesNotExistException();
        }
        community.channelAdded(channelDescriptor);

        return channel;
    }

    /**
     * Retrieves a channel.
     *
     * @param communityName the name of the community to which
     *                      the channel belongs
     * @param channelName the name of the channel requested
     * @return a remote reference to the requested channel
     * @throws ChannelDoesNotExistException if the channel does not exist
     * @throws RemoteException if an rmi error occurs
     */
    public final ServerChannel getChannel(final CommunityName communityName,
            final ChannelName channelName)
        throws ChannelDoesNotExistException, RemoteException {

        ServerChannel result = null;

        // Check if it exists
        if (channelExists(communityName, channelName)) {
            HashMap<ChannelName, ServerChannel> subMap = channelMap.get(communityName);

            result = subMap.get(channelName);
        } else {

            throw new ChannelDoesNotExistException("Channel '" +
                channelName.toString() + "' did not exist");
        }

        // Result should be non-null at this point
        if (result == null) throw new IllegalStateException("Got an unexpected null when looking for "
                + "a channel named " + channelName.getValue());

        return result;

    }

    /**
     * Checks whether a channel exists in a given community.
     *
     * @param communityName community name
     * @param channelName channel name
     * @return true if the community and channel both exist and both are not null,
     * false otherwise
     */
    public boolean channelExists(final CommunityName communityName,
            final ChannelName channelName) {

        if (channelMap.containsKey(communityName)) {
            HashMap<ChannelName, ServerChannel> subMap = channelMap.get(communityName);

            if (subMap != null &&
                    subMap.containsKey(channelName) &&
                    (subMap.get(channelName) != null)) {

                return true;

            }
        }

        return false;
    }
}
