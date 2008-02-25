package colab.server;

import java.rmi.RemoteException;

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
        groupSeven.getMembers().add(johannes);
        groupSeven.getMembers().add(pamela);
        groupSeven.getMembers().add(matthew);
        groupSeven.getMembers().add(chris);
        userManager.addCommunity(groupSeven);

        Community teamAwesome = new Community("Team Awesome", "awesomePass");
        teamAwesome.getMembers().add(chris);
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

}
