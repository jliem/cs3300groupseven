package colab.server;

import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import colab.common.remote.client.ColabClientRemote;
import colab.common.remote.server.ColabServerRemote;
import colab.common.remote.server.ConnectionRemote;
import colab.common.util.FileUtils;
import colab.server.channel.ChannelManager;
import colab.server.connection.Connection;
import colab.server.user.UserManager;

/**
 * Server implementation of ColabServerInterface.
 */
public class ColabServer extends UnicastRemoteObject
        implements ColabServerRemote {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

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

        this.userManager = new UserManager(this);
        this.channelManager = new ChannelManager(this);

    }

    /**
     * Constructs an instance of the server application.
     *
     * @param path the directory in which data is stored
     * @throws IOException if an rmi or file-io error occurs
     */
    public ColabServer(final String path) throws IOException {

        // Get the directory for data storage
        File dataDirectory = FileUtils.getDirectory(path);

        // Create the manager objects
        this.userManager = new UserManager(this,
                FileUtils.getDirectory(dataDirectory, "user"));
        this.channelManager = new ChannelManager(this,
                FileUtils.getDirectory(dataDirectory, "channel"));

    }

    /**
     * @param port the port on which to listen for connections
     * @throws IOException if a problem with rmi is encountered
     */
    public final void publish(final int port) throws IOException {

        // Create the rmi registry, add the server to it
        LocateRegistry.createRegistry(port);
        Naming.rebind("//localhost:" + port + "/COLAB_SERVER", this);

        System.out.println("Server initialized");

    }

    /** {@inheritDoc} */
    public final ConnectionRemote connect(final ColabClientRemote client)
            throws RemoteException {

        return new Connection(this, client);

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

        // Assign a security manager, in the event
        // that dynamic classes are loaded
        //if (System.getSecurityManager() == null) {
        //    System.setSecurityManager(new RMISecurityManager());
        //}

        // Get arguments
        String path = args[0];
        Integer port = Integer.parseInt(args[1]);

        // Create and initialize a server
        new ColabServer(path).publish(port);

    }

}
