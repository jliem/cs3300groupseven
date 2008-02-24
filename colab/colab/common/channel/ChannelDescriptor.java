package colab.common.channel;

import colab.common.naming.ChannelName;

public class ChannelDescriptor {

    private ChannelName channelName;
    private ChannelType channelType;

    public ChannelDescriptor(ChannelName channelName, ChannelType type) {
        this.channelName = channelName;
        this.channelType = type;
    }

    public ChannelDescriptor(String name, ChannelType type)
    {
        this(new ChannelName(name), type);
    }

    public ChannelName getName() {
        return channelName;
    }

    public void setName(ChannelName name) {
        this.channelName = name;
    }

    public ChannelType getType()
    {
        return channelType;
    }

    public void setType(ChannelType type)
    {
        this.channelType = type;
    }
}
