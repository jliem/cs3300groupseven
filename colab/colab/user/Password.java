package colab.user;

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
     * the given password string.
     *
     * @param pass a password string
     */
    public Password(final String pass) {
        this.hash = doHash(pass);
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
     * @param pass the password sting to check
     * @return true if the password is correct
     */
    public boolean checkPassword(final String pass) {
        return Arrays.equals(doHash(pass), hash);
    }

    /**
     * Performs a one-way hash on a string.
     *
     * @param str the string to hash
     * @return the result of the hash function
     */
    private static byte[] doHash(final String str) {
        byte[] result;
        digest.update(str.getBytes());
        result = digest.digest();
        digest.reset();
        return result;
    }

}
