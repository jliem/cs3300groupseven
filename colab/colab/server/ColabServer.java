package colab.server;

import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

import colab.common.channel.ChannelDescriptor;
import colab.common.channel.ChannelType;
import colab.common.exception.ChannelAlreadyExistsException;
import colab.common.exception.CommunityAlreadyExistsException;
import colab.common.exception.CommunityDoesNotExistException;
import colab.common.naming.ChannelName;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import colab.common.remote.client.ColabClientRemote;
import colab.common.remote.server.ColabServerRemote;
import colab.common.remote.server.ConnectionRemote;
import colab.common.util.FileUtils;
import colab.server.channel.ChannelManager;
import colab.server.connection.Connection;
import colab.server.user.Community;
import colab.server.user.Password;
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
     * The address in the rmi registry to which this
     * server has bound itself; null if not bound.
     */
    private String rmiAddress = null;

    /**
     * Constructs an instance of the server application.
     *
     * @throws RemoteException if an rmi error occurs
     */
    public ColabServer() throws RemoteException {

        this.userManager = new UserManager();
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

        // Create the user manager
        File userDirectory =
            FileUtils.getDirectory(dataDirectory, "user");
        this.userManager = new UserManager(userDirectory);

        // Create the channel manager
        File channelDirectory =
            FileUtils.getDirectory(dataDirectory, "channel");
        this.channelManager = new ChannelManager(this, channelDirectory);

    }

    /**
     * @param port the port on which to listen for connections
     * @throws IOException if a problem with rmi is encountered
     */
    public final void publish(final int port) throws IOException {

        String address = "//localhost:" + port + "/COLAB_SERVER";

        // Create the rmi registry, add the server to it
        try {
            System.out.print("Creating RMI registry on port " + port + ": ");
            LocateRegistry.createRegistry(port);
            System.out.println("Done");
        } catch (final ExportException e) {
            System.out.println("Already exists");
        }
        Naming.rebind(address, this);

        this.rmiAddress = address;

        System.out.println("Server initialized");

    }

    /**
     * Removes the server from the rmi registry.
     *
     * @throws NotBoundException if the server is not bound to the registry
     * @throws IOException if an I/O error occurs
     */
    public final void unpublish() throws NotBoundException, IOException {

        try {
            Naming.unbind(this.rmiAddress);
        } finally {
            this.rmiAddress = null;
        }

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
     * Creates a new community in this connection.
     *
     * @param name the community name
     * @param password the password used to join the community
     * @throws RemoteException if an rmi error occurs
     */
    public void createCommunity(final CommunityName communityName,
            final Password password) throws RemoteException {

        Community community = new Community(communityName,
                password);

        try {
            userManager.addCommunity(community);
        } catch (final CommunityAlreadyExistsException e) {
            throw new RemoteException(e.getMessage(), e);
        }

        // If the community was added successfully, add
        // the default lobby
        // Add a lobby to each community

        ChannelDescriptor lobbyDesc = new ChannelDescriptor(
                 new ChannelName("Lobby"), ChannelType.CHAT);

        try {
            channelManager.addChannel(communityName, lobbyDesc);
        } catch (final ChannelAlreadyExistsException e) {
            throw new RemoteException(e.getMessage(), e);
        } catch (CommunityDoesNotExistException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    /**
     * Adds a user as a member of a community.
     * @param userName the user
     * @param communityName the community
     * @throws RemoteException if an rmi error occurs
     */
    public void addAsMember(final UserName userName,
            final CommunityName communityName)
            throws RemoteException, CommunityDoesNotExistException {

        // Look up the community
        Community comm = userManager.getCommunity(communityName);

        if (comm != null) {
            comm.addMember(userName);
        }

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
