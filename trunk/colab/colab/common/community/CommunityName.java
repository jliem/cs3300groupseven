package colab.common.community;

import colab.common.identity.StringIdentifier;

/**
 * The name that uniquely identifies a community.
 */
public final class CommunityName extends StringIdentifier {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * Constructs a new community name.
     *
     * @param name the name of the community.
     */
    public CommunityName(final String name) {
        super(name);
    }

}
