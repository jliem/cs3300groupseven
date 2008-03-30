package colab.server.channel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import colab.common.channel.ChannelDescriptor;
import colab.common.exception.ChannelAlreadyExistsException;
import colab.common.exception.ChannelDoesNotExistException;
import colab.common.exception.CommunityDoesNotExistException;
import colab.common.naming.ChannelName;
import colab.common.naming.CommunityName;
import colab.common.util.FileUtils;
import colab.server.ColabServer;
import colab.server.user.Community;

/**
 * A channel manager provides channels.  This implementation
 * does not load any channel data into memory until a channel
 * is joined by one or more users.
 */
public final class ChannelManager {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /** Map of all channels, indexed by community name, then channel name. */
    private final Map<CommunityName,
                    Map<ChannelName, ServerChannel>> channelMap;

    /** The server this channel manager is running on. */
    private final ColabServer server;

    /**
     * The directory in which to store channel data.
     * Null if not saving any data.
     */
    private File baseDirectory;

    /**
     * Constructs a new channel manager.
     *
     * @param server the server this channel manager will be runnning on.
     */
    public ChannelManager(final ColabServer server) {
        this.server = server;
        this.channelMap =
            new HashMap<CommunityName, Map<ChannelName, ServerChannel>>();
    }

    /**
     * Constructs a new channel manager.
     *
     * @param server the server this channel manager will be runnning on.
     * @param directory the directory used to store channel information.
     */
    public ChannelManager(final ColabServer server, final File directory) {
        this(server);
        this.baseDirectory = directory;
        for (File communityDirectory : directory.listFiles()) {
            if (communityDirectory.getName().charAt(0) == '.') {
                continue;
            }
            CommunityName communityName =
                new CommunityName(communityDirectory.getName());
            Map<ChannelName, ServerChannel> subMap =
                new HashMap<ChannelName, ServerChannel>();
            channelMap.put(communityName, subMap);
            for (File channelFile : communityDirectory.listFiles()) {
                if (channelFile.getName().charAt(0) == '.') {
                    continue;
                }
                String[] nameBreakdown = channelFile.getName().split("\\.");
                ChannelName channelName = new ChannelName(nameBreakdown[0]);
                String channelType = nameBreakdown[1];
                ChannelDescriptor channelDescriptor =
                    new ChannelDescriptor(channelName, channelType);
                try {
                    this.addChannel(communityName, channelDescriptor);
                } catch (final CommunityDoesNotExistException e) {
                    System.exit(1); // should not happen
                } catch (final ChannelAlreadyExistsException e) {
                    System.exit(1); // should not happen
                }
            }
        }
    }

    /**
     * Returns a Collection of a Community's channels.
     *
     * @param communityName the Community to look up
     * @return a non-null Collection of its channels.
     *         If there are no channels, the resulting
     *         Collection will have a size of 0.
     */
    public Collection<ServerChannel> getChannels(
            final CommunityName communityName) {

        Collection<ServerChannel> result = null;

        if (channelMap.containsKey(communityName)) {

            // The community is in the map, so find what channels it has
            Map<ChannelName, ServerChannel> subMap =
                channelMap.get(communityName);

            result = subMap.values();

        }

        if (result == null) {
            // There are no channels, so return an empty list
            result = new ArrayList<ServerChannel>();
        }

        return result;
    }

    /**
     * Creates a new channel.
     *
     * @param communityName community name
     * @param channelDescriptor descriptor of the channel to add
     * @return the created channel object
     * @throws ChannelAlreadyExistsException if the parameters specify
     *             a channel that already exists
     * @throws CommunityDoesNotExistException if the specified community
     *             does not exist
     */
    public ServerChannel addChannel(final CommunityName communityName,
            final ChannelDescriptor channelDescriptor)
            throws ChannelAlreadyExistsException,
            CommunityDoesNotExistException {

        ChannelName channelName = channelDescriptor.getName();

        // Make sure the channel doesn't already exist
        if (channelExists(communityName, channelName)) {
            throw new ChannelAlreadyExistsException();
        }

        // Create the new channel
        ServerChannel channel;
        try {
            File file = null;
            if (this.baseDirectory != null) {
                File communityDirectory = FileUtils.getDirectory(baseDirectory,
                        communityName.getValue());
                String filename = channelName.getValue()
                        + "." + channelDescriptor.getType().toString();
                file = FileUtils.getFile(communityDirectory, filename);
            }
            channel = channelDescriptor.createServerChannel(file);
        } catch (final IOException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }

        // Check whether a community entry exists
        Map<ChannelName, ServerChannel> subMap = null;

        if (channelMap.containsKey(communityName)) {
            subMap = channelMap.get(communityName);
        } else {
            // No community entry, add it
            subMap = new HashMap<ChannelName, ServerChannel>();
            channelMap.put(communityName, subMap);
        }

        subMap.put(channelName, channel);

        // Notify the community that a channel has been added to it
        Community community = server.getCommunity(communityName);
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
     */
    public ServerChannel getChannel(final CommunityName communityName,
            final ChannelName channelName)
            throws ChannelDoesNotExistException {

        ServerChannel result = null;

        // Check if it exists
        if (channelExists(communityName, channelName)) {
            Map<ChannelName, ServerChannel> subMap =
                channelMap.get(communityName);
            result = subMap.get(channelName);
        } else {
            throw new ChannelDoesNotExistException(channelName);
        }

        // Result should be non-null at this point
        if (result == null) {
            throw new IllegalStateException(
                "Got an unexpected null when looking for "
                + "a channel named " + channelName.getValue());
        }

        return result;

    }

    /**
     * Checks whether a channel exists in a given community.
     *
     * @param communityName community name
     * @param channelName channel name
     * @return true if the community and channel both exist
     *         and both are not null, false otherwise
     */
    public boolean channelExists(final CommunityName communityName,
            final ChannelName channelName) {

        if (channelMap.containsKey(communityName)) {
            Map<ChannelName, ServerChannel> subMap =
                channelMap.get(communityName);

            if (subMap != null
                    && subMap.containsKey(channelName)
                    && (subMap.get(channelName) != null)) {

                return true;

            }
        }

        return false;

    }

}
