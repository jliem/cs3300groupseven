package colab.server;

import java.rmi.RemoteException;

import colab.common.channel.ChannelName;
import colab.common.community.CommunityName;
import junit.framework.TestCase;

public class ChannelManagerTester extends TestCase {

    private ChannelManager cm;

    public void testCreateChannelAndCommunity() throws RemoteException {
        cm = new ChannelManager();

        ServerChannel channel = cm.getChannel(new CommunityName("NewCommunity"),
                new ChannelName("NewChannel"));

        assert(channel != null);
    }

    public void testCreateChannel() throws RemoteException {
        cm = new ChannelManager();

        ServerChannel channel = cm.getChannel(new CommunityName("Community1"),
                new ChannelName("Channel1"));

        assert(channel != null);

        // Now that community exists, try again for a non-existing channel
        channel = cm.getChannel(new CommunityName("Community1"),
                new ChannelName("Channel2"));

        assert(channel != null);

    }


}