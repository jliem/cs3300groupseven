package colab.common.naming;

import colab.common.util.StringUtils;

/**
 * A utility class which holds all of the naming
 * conventions in one place to avoid conflicts.
 */
public final class ColabNameRules {

    /** Hidden default constructor. */
    private ColabNameRules() {
    }

    /** All letters and numbers. */
    private static final String ALPHANUMERIC =
          "0123456789"
        + "abcdefghijklmnopqrstuvwxyz"
        + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /** Characters that can be used in a community name. */
    private static final String COMMUNITY_CHARACTERS =
        ALPHANUMERIC + " _.-:,";

    /** The maximum length of a community name. */
    private static final int COMMUNITY_MAX_LENGTH = 50;

    /**
     * @return the maximum length of a community name
     */
    public static int getCommunityMaxLength() {
        return COMMUNITY_MAX_LENGTH;
    }

    /** Characters than can be used in a user name. */
    private static final String USER_CHARACTERS =
        ALPHANUMERIC + "._-";

    /** The maximum length of a user name. */
    private static final int USER_MAX_LENGTH = 25;

    /**
     * @return the maximum length of a user name
     */
    public static int getUserMaxLength() {
        return USER_MAX_LENGTH;
    }

    /**
     * Determines whether a string is a valid community name.
     *
     * @param str a potential community name
     * @return true if the name is valid, false otherwise
     */
    public static boolean isValidCommunityName(final String str) {
        return
               str.length() <= COMMUNITY_MAX_LENGTH
            && StringUtils.containsOnlyCharacters(str, COMMUNITY_CHARACTERS);
    }

    /**
     * Determines whether a string is a valid user name.
     *
     * @param str a potential user name
     * @return true if the name is valid, false otherwise
     */
    public static boolean isValidUserName(final String str) {
        return str.length() <= USER_MAX_LENGTH
            && StringUtils.containsOnlyCharacters(str, USER_CHARACTERS);
    }

}
