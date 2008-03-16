package colab.server.user;

import junit.framework.TestCase;

/**
 * Test cases for {@link User}.
 */
public final class UserTester extends TestCase {

    /**
     * Tests creating a user with the string constructor.
     */
    public void testStringConstructor() {

        User u = new User("Johannes", "password".toCharArray());
        assertEquals(u.getId().getValue(), "Johannes");

    }

}
