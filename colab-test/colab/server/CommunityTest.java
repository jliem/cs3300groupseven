package colab.server;

import java.rmi.RemoteException;

import junit.framework.TestCase;
import colab.server.Community;
import colab.common.naming.*;
import colab.client.ColabClient;
import colab.common.channel.*;

public class CommunityTest extends TestCase {

/*
    public void testCommunityCreationandClientAddition() throws RemoteException{
        Community comm = new Community(new CommunityName("TestComm"), new Password("TestPass"));
        ColabClient client = new ColabClient();

        comm.addClient(new UserName("TestUser"), client);

        assertSame(comm.getClients().get(new UserName("TestUser")), client);

    }
*/

/*
    public void testCommunityCreationandClientRemoval() throws RemoteException{
        Community comm = new Community(new CommunityName("TestComm"), new Password("TestPass"));
        ColabClient client = new ColabClient();

        comm.addClient(new UserName("TestUser"), client);

        comm.removeClient(new UserName("TestUser"));

        assertSame(comm.getClients().get(new UserName("TestUser")), null);

    }
*/

/*
    public void testCommunityCreationandChannelAddition() throws RemoteException{
        Community comm = new Community(new CommunityName("TestComm"), new Password("TestPass"));
        ColabClient client = new ColabClient();

        comm.addClient(new UserName("TestUser"), client);

        comm.channelAdded(new ChannelDescriptor("TestDesc", ChannelType.CHAT));

        assertSame(comm.getClients().get(new UserName("TestUser"))., null);

    }
*/

/*
    public void testCommunityAuthentication() throws RemoteException{
        Community comm = new Community(new CommunityName("TestComm"), new Password("TestPass"));
        ColabClient client = new ColabClient();

        comm.addClient(new UserName("TestUser"), client);
        comm.getMembers().add(new UserName("Bob"));

        assertTrue(comm.authenticate(new UserName("Bob"), new String("TestPass").toCharArray()));

    }
*/

}