package colab.common.user;

import colab.server.Password;
import junit.framework.TestCase;

/**
 * Tests for {@link Password}.
 */
public final class PasswordTester extends TestCase {

    /**
     * Creates a password, then checks the correct password.
     */
    public void testCorrectPassword() {
        Password p = new Password("jesus");
        assertTrue(p.checkPassword("jesus"));
    }

    /**
     * Creates a password, then checks an incorrect password.
     */
    public void testIncorrectPassword() {
        Password p = new Password("jesus");
        assertFalse(p.checkPassword("buddha"));
    }

}
