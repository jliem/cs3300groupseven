package colab.server;

import colab.common.channel.ChannelDescriptor;
import colab.common.channel.ChannelType;
import colab.common.naming.ChannelName;
import colab.server.channel.ChannelManager;
import colab.server.user.Community;
import colab.server.user.User;
import colab.server.user.UserManager;

public class MockColabServer extends ColabServer {

    public MockColabServer() throws Exception {

        UserManager userManager = getUserManager();
        ChannelManager channelManager = getChannelManager();

        // Populate a few test users

        User johannes = new User("Johannes", "pass1".toCharArray());
        userManager.addUser(johannes);

        User pamela = new User("Pamela", "pass2".toCharArray());
        userManager.addUser(pamela);

        User matthew = new User("Matthew", "pass3".toCharArray());
        userManager.addUser(matthew);

        User chris = new User("Chris", "pass4".toCharArray());
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
                 new ChannelName("Lobby"), ChannelType.CHAT);

        channelManager.addChannel(groupSeven.getId(), lobbyDesc);
        channelManager.addChannel(teamAwesome.getId(), lobbyDesc);
        channelManager.addChannel(noMembers.getId(), lobbyDesc);

    }

    public static void main(final String[] args) throws Exception {

        // Get arguments
        Integer port = Integer.parseInt(args[0]);

        // Create and initialize a server
        new MockColabServer().publish(port);

    }

}
