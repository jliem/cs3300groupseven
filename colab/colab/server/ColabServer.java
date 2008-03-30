package colab.server;

import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;

import colab.common.DebugManager;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.type.ChannelType;
import colab.common.channel.type.ChatChannelType;
import colab.common.exception.AuthenticationException;
import colab.common.exception.ChannelAlreadyExistsException;
import colab.common.exception.ChannelDoesNotExistException;
import colab.common.exception.CommunityAlreadyExistsException;
import colab.common.exception.CommunityDoesNotExistException;
import colab.common.exception.UserAlreadyExistsException;
import colab.common.naming.ChannelName;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import colab.common.remote.client.ColabClientRemote;
import colab.common.remote.server.ColabServerRemote;
import colab.common.remote.server.ConnectionRemote;
import colab.common.util.FileUtils;
import colab.server.channel.ChannelManager;
import colab.server.channel.ServerChannel;
import colab.server.connection.Connection;
import colab.server.user.Community;
import colab.server.user.Password;
import colab.server.user.User;
import colab.server.user.UserManager;

/**
 * Server implementation of ColabServerInterface.
 */
public class ColabServer extends UnicastRemoteObject
        implements ColabServerRemote {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    static {
        ChannelType.registerType("Chat", new ChatChannelType());
        ChannelType.registerType("Document", new ChatChannelType());
    }

    /**
     * The manager object that keeps track of users
     * and communities for this server instance.
     *
     * Field is protected so that MockColabServer can access it.
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
     * Creates a new community in this connection without specifying
     * any user as creator.
     *
     * @param communityName the community name
     * @param password the password used to join the community
     * @throws CommunityAlreadyExistsException if the community already exists
     */
    public final void createCommunity(final CommunityName communityName,
            final Password password) throws CommunityAlreadyExistsException {

        createCommunity(communityName, password, null);

    }

    /**
     * Creates a new community in this connection.
     *
     * @param communityName the community name
     * @param password the password used to join the community
     * @param creator the user who created this community
     * @throws CommunityAlreadyExistsException if the community already exists
     */
    public final void createCommunity(final CommunityName communityName,
            final Password password, final UserName creator)
            throws CommunityAlreadyExistsException {

        Community community = new Community(communityName,
                password);

        // TODO: The creator should also be a moderator
        // Add the creator as a member
        if (creator != null) {
            community.addMember(creator);
        }

        // Add the community
        userManager.addCommunity(community);

        // If the community was added successfully, add
        // the default lobby
        ChannelDescriptor lobbyDesc = new ChannelDescriptor(
                 new ChannelName("*** Lobby ***"), new ChatChannelType());

        try {
            createChannel(lobbyDesc, communityName);
        } catch (final ChannelAlreadyExistsException e) {
            DebugManager.shouldNotHappen(e);
            System.exit(1); // impossible
        } catch (final CommunityDoesNotExistException e) {
            DebugManager.shouldNotHappen(e);
            System.exit(1); // impossible
        }

    }

    /**
     * Creates a new channel.
     *
     * @param channelDescription the channel descriptor
     * @param communityName the community in which to create the channel
     * @throws CommunityDoesNotExistException if the community does not exist
     * @throws ChannelAlreadyExistsException if the channel already exists
     */
    public final void createChannel(final ChannelDescriptor channelDescription,
            final CommunityName communityName)
            throws CommunityDoesNotExistException,
            ChannelAlreadyExistsException {

        // Look up community to see if it exists
        getCommunity(communityName);

        channelManager.addChannel(communityName, channelDescription);

    }

    /**
     * Adds a new user.
     *
     * @param user the new user to add
     * @throws UserAlreadyExistsException if a user with the given
     *                                    name already exists
     */
    public final void addUser(final User user)
            throws UserAlreadyExistsException {

        userManager.addUser(user);
    }

    /**
     * Checks whether a user is a member of a given community.
     *
     * @param userName the user name
     * @param communityName the community name
     * @return true if the user is a member of the community, false otherwise
     * @throws CommunityDoesNotExistException if the community did not exist
     */
    public final boolean isMember(final UserName userName,
            final CommunityName communityName)
            throws CommunityDoesNotExistException {

        return userManager.getCommunity(communityName).isMember(userName);

    }

    /**
     * Checks that a user's password is correct.
     *
     * @param username a username
     * @param password a password
     * @return true if the user's password is correct
     * @throws AuthenticationException if the password is incorrect
     *                                 for the user
     */
    public final boolean checkPassword(final UserName username,
            final char[] password) throws AuthenticationException {

        return userManager.checkPassword(username, password);


    }

    /**
     * Retrieves a community.
     *
     * @param name the name of the community
     * @return the community with the given name
     * @throws CommunityDoesNotExistException
     * @throws CommunityDoesNotExistException if the community does not exist
     */
    public final Community getCommunity(final CommunityName name)
        throws CommunityDoesNotExistException {

        return userManager.getCommunity(name);

    }

    /**
     * Retrieves all of the communities on the server.
     *
     * @return a collection containing every community
     */
    public final Collection<Community> getAllCommunities() {

        return userManager.getAllCommunities();

    }

    /**
     * Returns a Collection of a Community's channels.
     *
     * @param communityName the Community to look up
     * @return a non-null Collection of its channels.
     *         If there are no channels, the resulting
     *         Collection will have a size of 0.
     */
    public final Collection<ServerChannel> getChannels(
            final CommunityName communityName) {

        return channelManager.getChannels(communityName);

    }

    /**
     * Retrieves a channel.
     *
     * @param communityName the name of the community to which
     *                      the channel belongs
     * @param channelName the name of the channel requested
     * @return a remote reference to the requested channel
     * @throws ChannelDoesNotExistException if the channel does not exist
     */
    public final ServerChannel getChannel(final CommunityName communityName,
            final ChannelName channelName) throws ChannelDoesNotExistException {

        return channelManager.getChannel(communityName, channelName);
    }

    /**
     * Returns the user/community manager.
     * Used for testing purposes in MockColabServer.
     *
     * @return the user manager for this server instance
     */
    protected final UserManager getUserManager() {
        return this.userManager;
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
