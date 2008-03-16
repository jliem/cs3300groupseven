package colab.common.exception;

import colab.common.naming.ChannelName;

/**
 * Indicates that an reference was made to a channel name
 * in a community which does not have a channel by that name.
 */
public class ChannelDoesNotExistException extends Exception {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs a new ChannelDoesNotExistException with a channel name.
     *
     * @param channelName the name of the requested and non-existent channel
     */
    public ChannelDoesNotExistException(final ChannelName channelName) {
        super("Channel " + channelName.getValue() + " does not exist.");
    }

}
