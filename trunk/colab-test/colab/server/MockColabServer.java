package colab.server;

import colab.common.naming.CommunityName;
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
        super.addAsMember(johannes.getId(), groupSeven.getId());
        super.addAsMember(pamela.getId(), groupSeven.getId());
        super.addAsMember(matthew.getId(), groupSeven.getId());
        super.addAsMember(chris.getId(), groupSeven.getId());

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
        super.addAsMember(chris.getId(), teamAwesome.getId());

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
