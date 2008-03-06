package colab.server;

import junit.framework.TestCase;

/**
 * Tests for {@link Password}.
 */
public final class PasswordTester extends TestCase {

    /**
     * Creates a password, then checks the correct password.
     */
    public void testCorrectPassword() {
        Password p = new Password("jesus".toCharArray());
        assertTrue(p.checkPassword("jesus".toCharArray()));
    }

    /**
     * Creates a password, then checks an incorrect password.
     */
    public void testIncorrectPassword() {
        Password p = new Password("jesus".toCharArray());
        assertFalse(p.checkPassword("buddha".toCharArray()));
    }

}
