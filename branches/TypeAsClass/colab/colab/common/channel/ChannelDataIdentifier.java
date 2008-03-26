package colab.common.channel;

import colab.common.identity.IntegerIdentifier;

/**
 * Identifier for channel data to uniquely identify data
 * elements within a channel, and to keep them in sequence.
 */
public class ChannelDataIdentifier extends IntegerIdentifier {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs an channel data identifier with a given value.
     *
     * @param integer the value of the identifying integer
     */
    public ChannelDataIdentifier(final Integer integer) {
        super(integer);
    }

    /**
     * Constructs a channel data identifier with a given value.
     *
     * @param integer the value of the identifying integer
     */
    public ChannelDataIdentifier(final int integer) {
        this(new Integer(integer));
    }

}
