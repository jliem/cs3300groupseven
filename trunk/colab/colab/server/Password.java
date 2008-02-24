package colab.server;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Represents a string password.  Stores only a hashed version of the string.
 */
public final class Password implements Serializable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * The hashed password.
     */
    private final byte[] hash;

    /**
     * A digest used to run the hash function.
     */
    private static MessageDigest digest;

    static {
        try {
            digest = java.security.MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a new Password representing
     * the given password character array.
     *
     * @param pass a character array
     */
    public Password(final char[] pass) {
        this.hash = doHash(pass);
    }

    /**
     * Constructs a new Password representing
     * the given password string.
     *
     * @param pass a password string
     */
    public Password(final String pass) {
        this(pass.toCharArray());
    }

    /**
     * Constructs a new Password whose hash is equal to
     * the given byte array.
     *
     * @param hash a hashed password
     */
    public Password(final byte[] hash) {
        this.hash = hash;
    }

    /**
     * Determines whether a password string matches
     * the password represented by this object.
     *
     * @param pass the password string to check
     * @return true if the password is correct
     */
    public boolean checkPassword(final char[] pass) {
        return Arrays.equals(doHash(pass), hash);
    }

    /**
     * Determines whether a password string matches
     * the password represented by this object.
     *
     * @param pass the password string to check
     * @return true if the password is correct
     */
    public boolean checkPassword(final String pass) {
        return checkPassword(pass.toCharArray());
    }

    /**
     * Performs a one-way hash on a string.
     *
     * @param characters the characters to hash
     * @return the result of the hash function
     */
    private static byte[] doHash(final char[] characters) {

        // Convert the char array to a byte array
        byte[] bytes = new byte[characters.length];
        for (int i = 0; i < characters.length; i++) {
            bytes[i] = (byte) characters[i];
        }

        // Hash the byte array
        byte[] result = doHash(bytes);

        // Clear the byte array for security
        clear(bytes);

        return result;

    }

    /**
     * Performs a one-way hash on a byte array.
     *
     * @param bytes the bytes to hash
     * @return the result of the hash function
     */
    private static byte[] doHash(final byte[] bytes) {

        byte[] result;
        digest.update((byte[]) bytes);
        result = digest.digest();
        digest.reset();
        return result;

    }

    /**
     * Clears an array to ensure that the contents do not remain in memory.
     *
     * @param bytes the array to clear
     */
    private static void clear(final byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = 0;
        }
    }

}
