package colab.server.user;

import junit.framework.TestCase;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;

/**
 * Test cases for {@link UserManager}.
 */
public final class UserManagerTest extends TestCase {

    /**
     * Adds a community to a UserManager, and retrieves it.
     *
     * @throws Exception if any exception is thrown
     */
    public void testAddCommunity() throws Exception {

        UserManager um = new UserManager();
        um.addCommunity(new Community(
                new CommunityName("TestComm"),
                new Password("TestCommPass".toCharArray())));

        assertEquals(
                um.getCommunity(new CommunityName("TestComm")).getId(),
                new CommunityName("TestComm"));

    }

    /**
     * Adds a user to a UserManager, and retrieves it.
     *
     * @throws Exception if any exception is thrown
     */
    public void testAddUser() throws Exception {

        UserManager um = new UserManager();
        um.addUser(new User(
                new UserName("TestUser"),
                new Password("TestUserPass".toCharArray())));

        assertEquals(
                um.getUser(new UserName("TestUser")).getId(),
                new UserName("TestUser"));

    }


}
