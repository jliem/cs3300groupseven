package colab.common.channel;

import colab.common.identity.StringIdentifier;

public class ChannelName extends StringIdentifier {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    public ChannelName() {
        super();
    }

    public ChannelName(final String name) {
        super(name);
    }

}
