package colab.common.event;

import colab.common.channel.ChannelDescriptor;

public class ChannelAddedEvent extends ChannelEvent {

    public ChannelAddedEvent(final ChannelDescriptor descriptor) {
        super(descriptor);
    }

}
