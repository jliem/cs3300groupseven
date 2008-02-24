package colab.common.channel;

import colab.common.identity.Identifiable;

/**
 * Represents a channel in the system, including data,
 * usernames and metadata.
 *
 * Each document or collaborative entity is a channel.
 * A channel represents content being edited, and the
 * workspace in which the group is dealing with it.
 */
public abstract class Channel implements Identifiable<ChannelName> {

    /**
     * The name of the channel.
     */
    private final ChannelName name;

    /**
     * Constructs a new channel.
     *
     * @param name the name of the channel
     */
    public Channel(final ChannelName name) {
        this.name = name;
    }

    /** {@inheritDoc} */
    public final ChannelName getId() {
        return name;
    }

    public abstract ChannelDescriptor getChannelDescriptor();
}
