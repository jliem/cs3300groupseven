package colab.common.event;

import colab.common.channel.ChannelDescriptor;

public class ChannelAddedEvent extends ChannelEvent {

    public ChannelAddedEvent(ChannelDescriptor descriptor) {
        super(descriptor);
    }

}
