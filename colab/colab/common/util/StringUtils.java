package colab.common.util;

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
     * @param str a string
     * @return true if the string is empty or null, false otherwise
     */
    public static boolean isEmptyOrNull(final String str) {
        return (str == null) || (str.equals(""));
    }

    /**
     * Checks whether every character in a string is present
     * in a valid characters set.
     *
     * @param str the string to validate
     * @param validCharacters a string containing the valid characters
     * @return true if the string is valid
     */
    public static boolean containsOnlyCharacters(
            final String str, final String validCharacters) {

        for (int i = 0; i < str.length(); i++) {
            if (validCharacters.indexOf(str.charAt(i)) == -1) {
                return false;
            }
        }

        return true;

    }

    /**
     * Converts a byte array, arbitrarily but consistently, to a string.
     *
     * @param bytes a byte array
     * @return a string which is dependent on the byte array
     */
    public static String toLetters(final byte[] bytes) {
        char[] charset = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        char[] letters = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            int index = Math.abs(new Integer(b)) % charset.length;
            char c = charset[index];
            letters[i] = c;
        }
        return new String(letters);
    }

}
