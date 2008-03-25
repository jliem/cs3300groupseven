package colab.server.user;

import junit.framework.TestCase;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;

/**
 * Test cases for {@link UserManager}.
 */
public final class UserManagerTest extends TestCase {

    public void testUserManagerCreationandAddCommunity() throws Exception {

        UserManager um = new UserManager();
        um.addCommunity(new Community(
                new CommunityName("TestComm"),
                new Password("TestCommPass".toCharArray())));

        assertEquals(
                um.getCommunity(new CommunityName("TestComm")).getId(),
                new CommunityName("TestComm"));

    }

    public void testUserManagerCreationandAddUser() throws Exception {

        UserManager um = new UserManager();
        um.addUser(new User(
                new UserName("TestUser"),
                new Password("TestUserPass".toCharArray())));

        assertEquals(
                um.getUser(new UserName("TestUser")).getId(),
                new UserName("TestUser"));

    }


}
