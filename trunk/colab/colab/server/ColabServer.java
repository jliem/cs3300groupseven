package colab.server;

import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import colab.community.Community;
import colab.server.remote.ColabServerInterface;
import colab.server.remote.ConnectionInterface;
import colab.user.User;
import colab.util.FileUtils;

/**
 * Server implementation of ColabServerInterface.
 */
public class ColabServer extends UnicastRemoteObject
        implements ColabServerInterface {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /** The default port. */
    public static final int DEFAULT_PORT = 9040;

    /**
     * The manager object that keeps track of users
     * and communities for this server instance.
     */
    private final UserManager userManager;

    /**
     * The manager object that keeps track of channels
     * for this server instance.
     */
    private final ChannelManager channelManager;

    /**
     * Constructs an instance of the server application.
     *
     * @throws RemoteException if an rmi error occurs
     */
    public ColabServer() throws RemoteException {

        // Create the manager objects
        this.userManager = new UserManager();
        this.channelManager = new ChannelManager();

    }

    /** {@inheritDoc} */
    public final ConnectionInterface connect() throws RemoteException {
        return new Connection(this);
    }

    /**
     * Returns the user/community manager.
     *
     * @return the user manager for this server instance
     */
    public final UserManager getUserManager() {
        return this.userManager;
    }

    /**
     * Returns the channel manager.
     *
     * @return the channel for this server instance
     */
    public final ChannelManager getChannelManager() {
        return this.channelManager;
    }

    /**
     * The entry point for launching the server application.
     *
     * @param args unused
     * @throws Exception if any exception is thrown
     */
    public static void main(final String[] args) throws Exception {

        File dataDirectory;
        try {
            dataDirectory = FileUtils.getOrCreateDirectory(args[0]);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }

        // Assign a security manager, in the event
        // that dynamic classes are loaded
        //if (System.getSecurityManager() == null) {
        //    System.setSecurityManager(new RMISecurityManager());
        //}

        // Create a server
        ColabServer server = new ColabServer();

        // Create the rmi registry, add the server to it
        LocateRegistry.createRegistry(DEFAULT_PORT);
        Naming.rebind("//localhost:" + DEFAULT_PORT + "/COLAB_SERVER", server);

        // Populate a few test communities
        server.userManager.addCommunity(
                new Community("Group Seven", "sevenPass"));
        server.userManager.addCommunity(
                new Community("Team Awesome", "awesomePass"));

        // Populate a few test users
        server.userManager.addUser(new User("Johannes", "pass1"));
        server.userManager.addUser(new User("Pamela", "pass2"));
        server.userManager.addUser(new User("Matthew", "pass3"));
        server.userManager.addUser(new User("Chris", "pass4"));

        System.out.println("Server initialized");

    }

}
