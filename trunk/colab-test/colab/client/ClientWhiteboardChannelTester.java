package colab.client;

import java.util.Set;

import junit.framework.TestCase;
import colab.common.event.user.UserJoinedEvent;
import colab.common.event.user.UserLeftEvent;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;

/**
 * Tester for {@link ClientWhiteboardChannelTester}.
 */
public final class ClientWhiteboardChannelTester extends TestCase {

    /**
     * Informs a channel that users have joined, and tests that
     * they get added to the channel's list of members.
     *
     * @throws Exception if any exception is thrown
     */
    public void testUserJoined() throws Exception {

         ClientWhiteboardChannel channel = new ClientWhiteboardChannel(
                 new ChannelName("Test Channel"));

         Set<UserName> members = channel.getMembers();
         UserName user1 = new UserName("test1");
         UserName user2 = new UserName("test2");

         assertTrue(members.isEmpty());

         channel.handleUserEvent(new UserJoinedEvent(user1));

         assertTrue(members.contains(user1));

         channel.handleUserEvent(new UserJoinedEvent(user2));

         assertTrue(members.contains(user2));

    }

    /**
     * Tests removing users from a channel's members list.
     *
     * @throws Exception if any exception is thrown
     */
    public void testUserLeft() throws Exception {
        ClientWhiteboardChannel channel = new ClientWhiteboardChannel(
                 new ChannelName("Test Channel"));

         Set<UserName> members = channel.getMembers();
         UserName user1 = new UserName("test1");
         UserName user2 = new UserName("test2");

         channel.handleUserEvent(new UserJoinedEvent(user1));

         channel.handleUserEvent(new UserJoinedEvent(user2));

         channel.handleUserEvent(new UserLeftEvent(user1));

         assertFalse(members.contains(user1));
         assertTrue(members.contains(user2));

         channel.handleUserEvent(new UserLeftEvent(user2));

         assertTrue(members.isEmpty());

    }

}
