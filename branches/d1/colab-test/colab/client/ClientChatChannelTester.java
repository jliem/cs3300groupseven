package colab.client;

import java.util.Set;

import junit.framework.TestCase;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;

public class ClientChatChannelTester extends TestCase {

    public void testUserJoined() throws Exception {
         ClientChatChannel chan = new ClientChatChannel(new ChannelName("Test Channel"));

         Set<UserName> members = chan.getMembers();
         UserName user1 = new UserName("test1");
         UserName user2 = new UserName("test2");

         assertTrue(members.isEmpty());

         chan.userJoined(user1);

         assertTrue(members.contains(user1));

         chan.userJoined(user2);

         assertTrue(members.contains(user2));

    }

    public void testUserLeft() throws Exception {
         ClientChatChannel chan = new ClientChatChannel(new ChannelName("Test Channel"));

         Set<UserName> members = chan.getMembers();
         UserName user1 = new UserName("test1");
         UserName user2 = new UserName("test2");

         chan.userJoined(user1);

         chan.userJoined(user2);

         chan.userLeft(user1);

         assertFalse(members.contains(user1));
         assertTrue(members.contains(user2));

         chan.userLeft(user2);

         assertTrue(members.isEmpty());
    }
}
