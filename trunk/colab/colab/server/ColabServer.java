package colab.server;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import colab.common.channel.ChannelDescriptor;
import colab.common.channel.ChannelType;
import colab.common.remote.client.ColabClientInterface;
import colab.common.remote.server.ColabServerInterface;
import colab.common.remote.server.ConnectionInterface;

/**
 * Server implementation of ColabServerInterface.
 */
final class ColabServer extends UnicastRemoteObject
        implements ColabServerInterface {

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

        // Create the manager objects
        this.userManager = new UserManager();
        this.channelManager = new ChannelManager(this);

    }

    /** {@inheritDoc} */
    public ConnectionInterface connect(final ColabClientInterface client)
            throws RemoteException {

        return new Connection(this, client);

    }

    /**
     * Returns the user/community manager.
     *
     * @return the user manager for this server instance
     */
    public UserManager getUserManager() {
        return this.userManager;
    }

    /**
     * Returns the channel manager.
     *
     * @return the channel for this server instance
     */
    public ChannelManager getChannelManager() {
        return this.channelManager;
    }

    private void initialize(final String path) throws IOException {
        //File dataDirectory = FileUtils.getOrCreateDirectory(path);
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

        // Create a server
        ColabServer server = new ColabServer();

        /*
        String pathArg;
        if (args.length >= 1) {
            pathArg = args[0];
        } else {
            pathArg = "data";
        }

        server.initialize(pathArg);
        */

        Integer port = null;
        if (args.length == 0) {
            System.err.println("No port specified");
            System.exit(1);
        } else {
            try {
                port = Integer.parseInt(args[0]);
            } catch (final NumberFormatException nfe) {
                System.err.println("Port must be an integer");
                System.exit(1);
            }
        }

        // Create the rmi registry, add the server to it
        LocateRegistry.createRegistry(port);
        Naming.rebind("//localhost:" + port + "/COLAB_SERVER", server);

        // Populate a few test users

        User johannes = new User("Johannes", "pass1");
        server.userManager.addUser(johannes);

        User pamela = new User("Pamela", "pass2");
        server.userManager.addUser(pamela);

        User matthew = new User("Matthew", "pass3");
        server.userManager.addUser(matthew);

        User chris = new User("Chris", "pass4");
        server.userManager.addUser(chris);

        // Populate a few test communities
        Community groupSeven = new Community("Group Seven", "sevenPass");
        groupSeven.getMembers().add(johannes);
        groupSeven.getMembers().add(pamela);
        groupSeven.getMembers().add(matthew);
        groupSeven.getMembers().add(chris);
        server.userManager.addCommunity(groupSeven);

        Community teamAwesome = new Community("Team Awesome", "awesomePass");
        teamAwesome.getMembers().add(chris);
        server.userManager.addCommunity(teamAwesome);

        Community noMembers = new Community("The No-Members Community", "abcd");
        server.userManager.addCommunity(noMembers);

        ChannelDescriptor lobbyDesc = new ChannelDescriptor(
                "Lobby", ChannelType.CHAT);

        server.channelManager.addChannel(groupSeven.getId(), lobbyDesc);
        server.channelManager.addChannel(teamAwesome.getId(), lobbyDesc);
        server.channelManager.addChannel(noMembers.getId(), lobbyDesc);

        System.out.println("Server initialized");

    }

}
