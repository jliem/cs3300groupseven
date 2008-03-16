package colab.common.exception;

/**
 * Indicates an attempt to create a new community when
 * a community with the provided name already exists.
 */
public class CommunityAlreadyExistsException extends Exception {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

}
