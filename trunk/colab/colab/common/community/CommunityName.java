package colab.common.community;

import colab.common.exception.naming.InvalidCommunityNameException;
import colab.common.identity.StringIdentifier;
import colab.naming.ColabNameRules;

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
        if (!ColabNameRules.isValidCommunityName(name)) {
            throw new InvalidCommunityNameException();
        }
    }

}
