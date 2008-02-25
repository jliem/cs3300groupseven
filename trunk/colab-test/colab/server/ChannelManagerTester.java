package colab.server;

import junit.framework.TestCase;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.ChannelType;
import colab.common.naming.ChannelName;
import colab.common.naming.CommunityName;
import colab.common.remote.exception.ChannelDoesNotExistException;

public class ChannelManagerTester extends TestCase {

    private ChannelManager cm;

    private ColabServer server;

    public void setUp() throws Exception {
        server = new MockColabServer();
    }

    public void testCreateChannelAndCommunity() throws Exception {
        cm = new ChannelManager(server);

        ServerChannel channel = cm.addChannel(new CommunityName("Group Seven"),
                new ChannelDescriptor("NewChannel", ChannelType.CHAT));

        assertNotNull(channel);
    }

    public void testCreateChannel() throws Exception {
        cm = new ChannelManager(server);

        ServerChannel channel = cm.addChannel(new CommunityName("Team Awesome"),
                new ChannelDescriptor("Channel1", ChannelType.CHAT));

        assertNotNull(channel);

        // Now that community exists, try again for a non-existing channel
        ChannelDoesNotExistException ce = null;
        try {
            channel = cm.getChannel(new CommunityName("Team Awesome"),
                new ChannelName("Channel2"));
        } catch (ChannelDoesNotExistException e) {
            ce = e;
        }
        assertNotNull(ce);

    }

    public void testGetChannel() throws Exception {
        cm = new ChannelManager(server);

        ServerChannel channel = cm.addChannel(new CommunityName("Team Awesome"),
                new ChannelDescriptor("Channel1", ChannelType.CHAT));

        assertNotNull(channel);

        // Make sure we can get it again
        ServerChannel channel2 = null;
        channel2 = cm.getChannel(new CommunityName("Team Awesome"),
            new ChannelName("Channel1"));

        assertEquals(channel, channel2);

    }


}
