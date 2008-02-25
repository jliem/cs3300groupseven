package colab.server;

import java.rmi.RemoteException;

import junit.framework.TestCase;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.ChannelType;
import colab.common.naming.ChannelName;
import colab.common.naming.CommunityName;
import colab.common.remote.exception.ChannelDoesNotExistException;

public class ChannelManagerTester extends TestCase {

    private ChannelManager cm;

    private ColabServer server;

    public void setUp() throws RemoteException {
        server = new ColabServer();
    }

    public void testCreateChannelAndCommunity() throws RemoteException {
        cm = new ChannelManager(server);

        ServerChannel channel = cm.addChannel(new CommunityName("NewCommunity"),
                new ChannelDescriptor("NewChannel", ChannelType.CHAT));

        assertNotNull(channel);
    }

    public void testCreateChannel() throws RemoteException {
        cm = new ChannelManager(server);

        ServerChannel channel = cm.addChannel(new CommunityName("Community1"),
                new ChannelDescriptor("Channel1", ChannelType.CHAT));

        assertNotNull(channel);

        // Now that community exists, try again for a non-existing channel
        try {
            channel = cm.getChannel(new CommunityName("Community1"),
                new ChannelName("Channel2"));
        } catch (ChannelDoesNotExistException ce) {
            assertNotNull(ce);
        }

    }

    public void testGetChannel() throws RemoteException {
        cm = new ChannelManager(server);

        ServerChannel channel = cm.addChannel(new CommunityName("Community1"),
                new ChannelDescriptor("Channel1", ChannelType.CHAT));

        assertNotNull(channel);

        // Make sure we can get it again
        ServerChannel channel2 = null;
        try {
            channel2 = cm.getChannel(new CommunityName("Community1"),
                new ChannelName("Channel1"));
        } catch (ChannelDoesNotExistException ce) {
            // If we get an exception here, we fail
            assertNull(ce);
        }

        assertEquals(channel, channel2);

    }


}
