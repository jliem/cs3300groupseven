package colab.server.user;

import junit.framework.TestCase;
import colab.client.ColabClient;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;

/**
 * Test cases for {@link Community}.
 */
public final class CommunityTest extends TestCase {


    /**
     * Tests community password.
     *
     * @throws Exception
     */
    public void testCommunityPassword() throws Exception {

        CommunityName name = new CommunityName("TestComm");
        Password pass = new Password("TestPass");
        Community community = new Community(
                name, pass);


        assertEquals(community.getPassword(), pass);

    }

    /**
     * Tests community name.
     *
     * @throws Exception
     */
    public void testCommunityName() throws Exception {

        CommunityName name = new CommunityName("TestComm");
        Password pass = new Password("TestPass");
        Community community = new Community(
                name, pass);


        assertEquals(community.getId(), name);

    }


/*
    public void testCommunityCreationandClientRemoval() throws RemoteException {
        Community comm = new Community(
            new CommunityName("TestComm"), new Password("TestPass"));
        ColabClient client = new ColabClient();

        comm.addClient(new UserName("TestUser"), client);

        comm.removeClient(new UserName("TestUser"));

        assertSame(comm.getClients().get(new UserName("TestUser")), null);

    }
*/

/*
    public void testCommunityCreationandChannelAddition()
            throws RemoteException {
        Community comm = new Community(
            new CommunityName("TestComm"), new Password("TestPass"));
        ColabClient client = new ColabClient();

        comm.addClient(new UserName("TestUser"), client);

        comm.channelAdded(new ChannelDescriptor("TestDesc", ChannelType.CHAT));

        assertSame(comm.getClients().get(new UserName("TestUser"))., null);

    }
*/

/*
    public void testCommunityAuthentication() throws RemoteException{
        Community comm = new Community(
            new CommunityName("TestComm"), new Password("TestPass"));
        ColabClient client = new ColabClient();

        comm.addClient(new UserName("TestUser"), client);
        comm.getMembers().add(new UserName("Bob"));

        assertTrue(comm.authenticate(
            new UserName("Bob"), new String("TestPass").toCharArray()));

    }
*/

}
