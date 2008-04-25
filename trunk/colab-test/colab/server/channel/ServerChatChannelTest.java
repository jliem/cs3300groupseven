package colab.server.channel;

import junit.framework.TestCase;
import colab.common.channel.ChannelDataIdentifier;
import colab.common.channel.chat.ChatChannelData;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;

/**
 * Test cases for {@link ServerChatChannel}.
 */
public final class ServerChatChannelTest extends TestCase {

    /**
     * Adds data to a channel.
     *
     * @throws Exception if any exception is thrown
     */
    public void testAdd() throws Exception {

        ServerChatChannel scChannel = new ServerChatChannel(
                new ChannelName("TestChatChannel"));

        scChannel.add(new ChatChannelData(
                new ChannelDataIdentifier(1),
                "Test text",
                new UserName("TestChatter")));

        ChatChannelData data =
            (ChatChannelData) scChannel.getLastData(1).get(0);
        assertEquals(data.getText(), "Test text");
    }

    /**
     * Adds data to a channel, and tests getLastData().
     *
     * @throws Exception if any exception is thrown
     */
    public void testGetLastData() throws Exception {
        ServerChatChannel scChannel = new ServerChatChannel(
                new ChannelName("TestChatChannel"));

        scChannel.add(new ChatChannelData(
                new ChannelDataIdentifier(1),
                "Text1",
                new UserName("TestChatter")));
        Thread.sleep(100);
        scChannel.add(new ChatChannelData(
                new ChannelDataIdentifier(2),
                "Text2",
                new UserName("TestChatter")));

        ChatChannelData data =
            (ChatChannelData) scChannel.getLastData(1).get(0);

        assertEquals(data.getText(), "Text2");

    }

}
