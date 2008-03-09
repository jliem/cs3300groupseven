package colab.common.channel;

import java.io.Serializable;

import colab.common.naming.ChannelName;

public class ChannelDescriptor implements Serializable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private ChannelName channelName;

    private ChannelType channelType;

    public ChannelDescriptor(
            final ChannelName channelName, final ChannelType type) {

        this.channelName = channelName;
        this.channelType = type;

    }

    public ChannelDescriptor(
            final String name, final ChannelType type) {

        this(new ChannelName(name), type);

    }

    public ChannelName getName() {
        return channelName;
    }

    public void setName(final ChannelName name) {
        this.channelName = name;
    }

    public ChannelType getType() {
        return channelType;
    }

    public void setType(final ChannelType type) {
        this.channelType = type;
    }

    public String toString() {
        return this.channelName.toString() + ": " + this.channelType.toString();
    }
}
