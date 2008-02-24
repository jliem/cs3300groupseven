package colab.common.naming;

import colab.common.channel.Channel;
import colab.common.identity.StringIdentifier;

/**
 * Represents the name of a {@link Channel}.
 */
public final class ChannelName extends StringIdentifier {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs an empty channel name.
     */
    public ChannelName() {
        super();
    }

    /**
     * Constructs a channel name.
     *
     * @param name the name of the channel
     */
    public ChannelName(final String name) {
        super(name);
    }

}
