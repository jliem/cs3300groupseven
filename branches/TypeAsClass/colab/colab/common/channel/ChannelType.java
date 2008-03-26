package colab.common.channel;

/**
 * Represents the types of {@link Channel}s.
 */
public enum ChannelType {

    /** A chat room. */
    CHAT("Chat"),

    /** A collaborative document editor. */
    DOCUMENT("Document"),

    /** A collaborative drawing tool. */
    WHITE_BOARD("White Board");

    /** A human-readable string representing the channel type. */
    private final String string;

    /**
     * Constructs a new ChannelType.
     *
     * @param string a human-readable string
     */
    private ChannelType(final String string) {
        this.string = string;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return string;
    }

}
