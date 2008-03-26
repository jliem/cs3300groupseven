package colab.server.channel;

import java.rmi.RemoteException;

import junit.framework.TestCase;
import colab.server.channel.ServerChatChannel;
import colab.common.naming.*;
import colab.common.channel.*;

public final class ServerChatChannelTest extends TestCase {

    public void testServerChatChannelCreationandAddition()
            throws RemoteException {

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

    public void testGetLastData() throws Exception{
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
