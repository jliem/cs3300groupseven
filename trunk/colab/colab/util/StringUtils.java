package colab.util;

/**
 * A utility class containing methods for dealing with strings.
 */
public final class StringUtils {

    /** Hidden default constructor. */
    private StringUtils() {
    }

    /**
     * Returns a given string, or an empty string if null.
     *
     * @param str a string
     * @return str, or an empty string if str is null
     */
    public static String emptyIfNull(final String str) {
        if (str == null) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * Determines whether a string is empty or null.
     *
     * @param str a stirng
     * @return true if the string is empty or null, false otherwise
     */
    public static boolean isEmptyOrNull(final String str) {
        return (str == null) || (str.equals(""));
    }

}
