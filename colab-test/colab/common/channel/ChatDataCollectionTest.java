package colab.common.channel;

import java.rmi.RemoteException;
import java.util.List;

import junit.framework.TestCase;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;
import colab.server.channel.ServerChatChannel;

public final class ChatDataCollectionTest extends TestCase {

    public void testServerChatChannelCreationandAddition()
            throws RemoteException {

        ServerChatChannel scChannel =
            new ServerChatChannel(new ChannelName("TestChatChannel"));

        scChannel.add(new ChatChannelData(
                new ChannelDataIdentifier(1),
                "Test text",
                new UserName("TestChatter")));

        ChatChannelData data =
            (ChatChannelData) scChannel.getLastData(1).get(0);
        assertEquals(data.getText(), "Test text");

    }

    public void testGetLastData() throws Exception {
        ServerChatChannel scChannel =
            new ServerChatChannel(new ChannelName("TestChatChannel"));

        scChannel.add(new ChatChannelData(
                new ChannelDataIdentifier(1),
                "Text1",
                new UserName("TestChatter")));
        Thread.sleep(100);
        scChannel.add(new ChatChannelData(
                new ChannelDataIdentifier(2),
                "Text2",
                new UserName("TestChatter")));
        Thread.sleep(100);
        scChannel.add(new ChatChannelData(
                new ChannelDataIdentifier(3),
                "Text3",
                new UserName("TestChatter")));

        List<ChatChannelData> last2 = scChannel.getLastData(2);

        assertEquals(last2.get(0).getText(), "Text2");
        assertEquals(last2.get(1).getText(), "Text3");

    }

    public void testGetAllData() throws Exception {
        ServerChatChannel scChannel =
            new ServerChatChannel(new ChannelName("TestChatChannel"));

        scChannel.add(new ChatChannelData(
                new ChannelDataIdentifier(7),
                "Text1",
                new UserName("TestChatter")));
        Thread.sleep(100);
        scChannel.add(new ChatChannelData(
                new ChannelDataIdentifier(8),
                "Text2",
                new UserName("TestChatter")));
        Thread.sleep(100);
        scChannel.add(new ChatChannelData(
                new ChannelDataIdentifier(9),
                "Text3",
                new UserName("TestChatter")));

        List<ChatChannelData> all = scChannel.getLastData(-1);

        assertEquals(all.get(0).getText(), "Text1");
        assertEquals(all.get(1).getText(), "Text2");
        assertEquals(all.get(2).getText(), "Text3");

    }

}
