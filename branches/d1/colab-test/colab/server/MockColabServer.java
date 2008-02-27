package colab.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import colab.common.channel.ChannelDescriptor;
import colab.common.channel.ChannelType;

public class MockColabServer extends ColabServer {

    public MockColabServer() throws RemoteException {
        super();

        // Populate a few test users

        User johannes = new User("Johannes", "pass1");
        userManager.addUser(johannes);

        User pamela = new User("Pamela", "pass2");
        userManager.addUser(pamela);

        User matthew = new User("Matthew", "pass3");
        userManager.addUser(matthew);

        User chris = new User("Chris", "pass4");
        userManager.addUser(chris);

        // Populate a few test communities

        Community groupSeven = new Community("Group Seven", "sevenPass");
        groupSeven.getMembers().add(johannes.getId());
        groupSeven.getMembers().add(pamela.getId());
        groupSeven.getMembers().add(matthew.getId());
        groupSeven.getMembers().add(chris.getId());
        userManager.addCommunity(groupSeven);

        Community teamAwesome = new Community("Team Awesome", "awesomePass");
        teamAwesome.getMembers().add(chris.getId());
        userManager.addCommunity(teamAwesome);

        Community noMembers = new Community("The No-Members Community", "abcd");
        userManager.addCommunity(noMembers);

        // Add a lobby to each community

        ChannelDescriptor lobbyDesc = new ChannelDescriptor(
                "Lobby", ChannelType.CHAT);

        channelManager.addChannel(groupSeven.getId(), lobbyDesc);
        channelManager.addChannel(teamAwesome.getId(), lobbyDesc);
        channelManager.addChannel(noMembers.getId(), lobbyDesc);

    }

    public static void main(final String[] args) throws Exception {

        // Assign a security manager, in the event
        // that dynamic classes are loaded
        //if (System.getSecurityManager() == null) {
        //    System.setSecurityManager(new RMISecurityManager());
        //}

        // Create a server
        ColabServer server = new MockColabServer();

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

        System.out.println("Server initialized");

    }

}
