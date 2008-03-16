package colab.common.exception;

/**
 * Indicates an attempt to add a channel to a community when
 * the community already has a channel by the same name.
 */
public class ChannelAlreadyExistsException extends Exception {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

}
