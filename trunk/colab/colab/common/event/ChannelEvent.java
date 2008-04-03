package colab.common.event;

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
