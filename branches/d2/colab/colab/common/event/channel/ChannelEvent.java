package colab.common.event.channel;

import colab.common.channel.ChannelDescriptor;

public class ChannelEvent {

    private ChannelDescriptor descriptor;

    public ChannelEvent(final ChannelDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public ChannelDescriptor getChannelDescriptor() {
        return descriptor;
    }
}
