package colab.server;

import java.rmi.RemoteException;

import junit.framework.TestCase;
import colab.server.ServerChatChannel;
import colab.common.naming.*;
import colab.common.channel.*;

public class ServerChatChannelTest extends TestCase {

	public void testServerChatChannelCreationandAddition() throws RemoteException{
		ServerChatChannel scChannel = new ServerChatChannel(new ChannelName("TestChatChannel"));
		
		scChannel.add(new ChatChannelData("Test text", new UserName("TestChatter")));
		
		ChatChannelData data = (ChatChannelData) scChannel.getLastData(1).get(0);
		assertEquals(data.getText(), "Test text");
	}
}
