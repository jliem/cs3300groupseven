package colab.common.util;

import java.util.Random;

/**
 * A utility class containing methods for dealing with strings.
 */
public final class StringUtils {

    private static final Random RANDOM = new Random();

    /** All letters and numbers. */
    private static final String ALPHANUMERIC =
          "0123456789"
        + "abcdefghijklmnopqrstuvwxyz"
        + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

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

    /**
     * Concatenates a string with itself repeatly and returns the result.
     *
     * @param str a string to repeat
     * @param times the number of times to repeat it
     * @return the given string, repeated the given number of times
     */
    public static String repeat(final String str, final int times) {
        if (times < 0) {
            throw new IllegalArgumentException();
        }
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < times; i++) {
            out.append(str);
        }
        return out.toString();
    }

    /**
     * Picks a random character out of a string.
     *
     * @param charSet the string of characters to choose from
     * @return a randomly chosen character
     */
    public static char random(final String charSet) {
        int index = RANDOM.nextInt(charSet.length());
        char character = charSet.charAt(index);
        return character;
    }

    /**
     * Generates a random string using the specified character set.
     *
     * @param count the desired string length
     * @param charSet the characters to choose from
     * @return a randomly generated string
     */
    public static String random(final int count, final String charSet) {

        StringBuilder str = new StringBuilder();

        for (int i = 0; i < count; i++) {
            str.append(random(charSet));
        }

        return str.toString();

    }

    /**
     * Generates a random alphanumeric string.
     *
     * @param count the desired string length
     * @return a random alphanumeric string
     */
    public static String randomAlphanumeric(final int count) {

        return random(count, ALPHANUMERIC);

    }

}
