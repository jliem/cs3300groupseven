package colab.common.channel;

import java.io.Serializable;

import colab.common.naming.ChannelName;

/**
 * Descriptive data used to identify a channel.
 */
public final class ChannelDescriptor implements Serializable {

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
        this.channelType = type;

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

    /** {@inheritDoc} */
    @Override
    public String toString() {

        // Because ChannelManagerPanel uses the toString() in the display,
        // it needs to be as short and concise as possible
        return this.channelName.toString() + " - " + this.channelType.toString();

    }

}
