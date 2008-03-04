package colab.common.channel;

import java.rmi.RemoteException;
import java.util.List;

import junit.framework.TestCase;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;
import colab.server.channel.ServerChatChannel;

public class ChatDataCollectionTest extends TestCase {

    public void testServerChatChannelCreationandAddition()
            throws RemoteException {

        ServerChatChannel scChannel =
            new ServerChatChannel(new ChannelName("TestChatChannel"));

        scChannel.add(
                new ChatChannelData("Test text", new UserName("TestChatter")));

        ChatChannelData data =
            (ChatChannelData) scChannel.getLastData(1).get(0);
        assertEquals(data.getText(), "Test text");

    }

    public void testGetLastData() throws Exception {
        ServerChatChannel scChannel =
            new ServerChatChannel(new ChannelName("TestChatChannel"));

        scChannel.add(
                new ChatChannelData("Text1", new UserName("TestChatter")));
        Thread.sleep(100);
        scChannel.add(
                new ChatChannelData("Text2", new UserName("TestChatter")));
        Thread.sleep(100);
        scChannel.add(
                new ChatChannelData("Text3", new UserName("TestChatter")));

        List<ChannelData> last2 = scChannel.getLastData(2);

        assertEquals(((ChatChannelData) last2.get(0)).getText(), "Text2");
        assertEquals(((ChatChannelData) last2.get(1)).getText(), "Text3");

    }

    public void testGetAllData() throws Exception {
        ServerChatChannel scChannel =
            new ServerChatChannel(new ChannelName("TestChatChannel"));

        scChannel.add(
                new ChatChannelData("Text1", new UserName("TestChatter")));
        Thread.sleep(100);
        scChannel.add(
                new ChatChannelData("Text2", new UserName("TestChatter")));
        Thread.sleep(100);
        scChannel.add(
                new ChatChannelData("Text3", new UserName("TestChatter")));

        List<ChannelData> all = scChannel.getLastData(-1);

        assertEquals(((ChatChannelData) all.get(0)).getText(), "Text1");
        assertEquals(((ChatChannelData) all.get(1)).getText(), "Text2");
        assertEquals(((ChatChannelData) all.get(2)).getText(), "Text3");

    }

}
