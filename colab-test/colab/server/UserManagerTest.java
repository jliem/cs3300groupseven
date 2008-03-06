package colab.server;

import colab.common.naming.*;
import junit.framework.TestCase;

public class UserManagerTest extends TestCase {

    public void testUserManagerCreationandAddCommunity() throws Exception{

        MockColabServer server = new MockColabServer();
        UserManager um = new UserManager(server);
        um.addCommunity(new Community(
                new CommunityName("TestComm"),
                new Password("TestCommPass".toCharArray())));

        assertEquals(
                um.getCommunity(new CommunityName("TestComm")).getId(),
                new CommunityName("TestComm"));

    }

    public void testUserManagerCreationandAddUser() throws Exception{

        MockColabServer server = new MockColabServer();
        UserManager um = new UserManager(server);
        um.addUser(new User(
                new UserName("TestUser"),
                new Password("TestUserPass".toCharArray())));

        assertEquals(
                um.getUser(new UserName("TestUser")).getId(),
                new UserName("TestUser"));
    }


}
