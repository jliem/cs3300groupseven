package colab.client;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import junit.framework.TestCase;
import colab.common.exception.UnableToConnectException;
import colab.server.MockColabServer;

public class ColabClientTester extends TestCase {

    public void testConnect() throws Exception {

        MockColabServer server = new MockColabServer();
        // Create the rmi registry, add the server to it
        Integer port = new Integer(9040);

        LocateRegistry.createRegistry(port);
        Naming.rebind("//localhost:" + port + "/COLAB_SERVER", server);

        ColabClient client = new ColabClient();

        UnableToConnectException ue = null;

        try {
            client.connect("bad server");
        } catch (UnableToConnectException e) {
            ue = e;
        }

        assertNotNull(ue);

        ue = null;

        try {
            client.connect("localhost:9040");
        } catch (UnableToConnectException e) {
            ue = e;
        }

        assertNull(ue);

    }

}
