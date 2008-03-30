package colab.common.channel;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import colab.common.channel.type.ChannelType;
import colab.common.naming.ChannelName;
import colab.server.channel.ServerChannel;

/**
 * Descriptive data used to identify a channel.
 */
public final class ChannelDescriptor implements Serializable,
    Comparable<ChannelDescriptor> {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /** The name of the channel. */
    private ChannelName channelName;

    /** The type of channel. */
    private ChannelType channelType;

    /**
     * Constructs a new ChannelDescriptor.
     *
     * @param channelName the name of the channel
     * @param type the type of channel
     */
    public ChannelDescriptor(
            final ChannelName channelName, final ChannelType type) {

        this.channelName = channelName;

        if (type == null) {
            throw new IllegalArgumentException("Can't create a channel "
                    + "with type null!");
        }

        this.channelType = type;

    }

    public ChannelDescriptor(final ChannelName channelName, final String type) {

        this(channelName, ChannelType.get(type));

    }

    /**
     * Retrieves the name of the channel.
     *
     * @return the name of the channel
     */
    public ChannelName getName() {
        return channelName;
    }

    /**
     * Sets the name of the channel.
     *
     * @param name the name of the channel
     */
    public void setName(final ChannelName name) {
        this.channelName = name;
    }

    /**
     * Retrieves the type of channel.
     *
     * @return the type of channel
     */
    public ChannelType getType() {
        return channelType;
    }

    /**
     * Sets the type of channel.
     *
     * @param type the type of channel
     */
    public void setType(final ChannelType type) {
        this.channelType = type;
    }

    /**
     * Constructs a new ServerChannel.
     *
     * @return the created channel
     */
    public ServerChannel createServerChannel() {

        return channelType.createServerChannel(channelName);

    }

    /**
     * Constructs a new ServerChannel.
     *
     * If file is null, data will not be stored persistently;
     * createServerChannel(null) is equivalent to createServerChannel().
     *
     * @param file a file to use for persistent data storage
     * @return the created channel
     * @throws IOException if a file storage error occurs
     */
    public ServerChannel createServerChannel(final File file)
            throws IOException {

        if (file == null) {
            return createServerChannel();
        }

        return channelType.createServerChannel(channelName, file);

    }

    /** {@inheritDoc} */
    @Override
    public String toString() {

        // Because ChannelManagerPanel uses the toString() in the display,
        // it needs to be as short and concise as possible
        return this.channelName.toString() + " - "
                + this.channelType.toString();

    }

    /** {@inheritDoc} */
    public int compareTo(final ChannelDescriptor cd) {
        // Sort by name
        return this.getName().compareTo(cd.getName());
    }

    /**
     * Checks whether an object is equal to this ChannelDescriptor.
     * Equality is defined as having the same name, so the community
     * of the channel is not taken into account.
     *
     * @param o the Object to check
     * @return true if the objects are equal
     */
    @Override
    public boolean equals(final Object o) {

        if (!(o instanceof ChannelDescriptor)) {
            return false;
        }

        ChannelDescriptor cd = (ChannelDescriptor) o;
        boolean nameEquals = channelName.equals(cd.getName());
        boolean typeEquals = channelType.equals(cd.getType());
        return (nameEquals && typeEquals);

    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {

        return channelName.hashCode() + channelType.hashCode();

    }

}
