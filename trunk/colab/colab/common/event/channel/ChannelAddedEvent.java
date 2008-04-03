package colab.common.event.channel;

import colab.common.channel.ChannelDescriptor;

public class ChannelAddedEvent extends ChannelEvent {

    public ChannelAddedEvent(final ChannelDescriptor descriptor) {
        super(descriptor);
    }

}
