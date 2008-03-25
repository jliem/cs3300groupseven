package colab.server;

import java.rmi.RemoteException;

import colab.common.exception.CommunityDoesNotExistException;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import colab.server.channel.ChannelManager;
import colab.server.user.Community;
import colab.server.user.Password;
import colab.server.user.User;
import colab.server.user.UserManager;

public final class MockColabServer extends ColabServer {

    public MockColabServer() throws Exception {
//
//        UserManager userManager = getUserManager();
//        ChannelManager channelManager = getChannelManager();

        // Populate a few test users

        User johannes = new User("Johannes", "pass1".toCharArray());
        addUser(johannes);

        User pamela = new User("Pamela", "pass2".toCharArray());
        addUser(pamela);

        User matthew = new User("Matthew", "pass3".toCharArray());
        addUser(matthew);

        User chris = new User("Chris", "pass4".toCharArray());
        addUser(chris);

        // Populate a few test communities

        Community groupSeven = new Community("Group Seven", "sevenPass");
        super.createCommunity(groupSeven.getId(),
                groupSeven.getPassword());
        this.addAsMember(johannes.getId(), groupSeven.getId());
        this.addAsMember(pamela.getId(), groupSeven.getId());
        this.addAsMember(matthew.getId(), groupSeven.getId());
        this.addAsMember(chris.getId(), groupSeven.getId());

//        Community groupSeven = new Community("Group Seven", "sevenPass");
//        groupSeven.getMembers().add(johannes.getId());
//        groupSeven.getMembers().add(pamela.getId());
//        groupSeven.getMembers().add(matthew.getId());
//        groupSeven.getMembers().add(chris.getId());
//        userManager.addCommunity(groupSeven);
//
//
//        teamAwesome.getMembers().add(chris.getId());
//        userManager.addCommunity(teamAwesome);

        Community teamAwesome = new Community("Team Awesome", "awesomePass");
        super.createCommunity(teamAwesome.getId(),
                teamAwesome.getPassword());
        this.addAsMember(chris.getId(), teamAwesome.getId());

        Community noMembers = new Community("The No-Members Community", "abcd");
        super.createCommunity(noMembers.getId(),
                noMembers.getPassword());

//
//        // Add a lobby to each community
//
//        ChannelDescriptor lobbyDesc = new ChannelDescriptor(
//                 new ChannelName("Lobby"), ChannelType.CHAT);
//
//        channelManager.addChannel(groupSeven.getId(), lobbyDesc);
//        channelManager.addChannel(teamAwesome.getId(), lobbyDesc);
//        channelManager.addChannel(noMembers.getId(), lobbyDesc);

    }

    /**
     * Adds a user as a member of a community.
     *
     * @param userName the user
     * @param communityName the community
     * @throws RemoteException if an rmi error occurs
     */
    public void addAsMember(final UserName userName,
            final CommunityName communityName)
            throws RemoteException, CommunityDoesNotExistException {

        // Look up the community
        UserManager userManager = super.getUserManager();
        Community comm = userManager.getCommunity(communityName);
        if (comm != null)
            comm.addMember(userName);

    }

    public static void main(final String[] args) throws Exception {


        // Default port
        Integer port = 9040;

        // Get arguments
        if (args.length > 0)
            port = Integer.parseInt(args[0]);


        // Create and initialize a server
        new MockColabServer().publish(port);

    }

}
