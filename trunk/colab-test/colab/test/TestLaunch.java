package colab.test;

import java.rmi.Naming;

import colab.client.ClientChatChannel;
import colab.client.ColabClient;
import colab.common.channel.ChannelName;
import colab.common.community.CommunityName;
import colab.common.user.UserName;
import colab.server.ColabServer;
import colab.server.remote.ColabServerInterface;
import colab.server.remote.ConnectionInterface;

public class TestLaunch {

    public static void main(String[] args) throws Exception {
        ColabServer.main(new String[]{});
        Thread.sleep(1000L);

        ColabClient client = new ColabClient();

        String url = "//localhost:" + 9040 + "/COLAB_SERVER";

        ColabServerInterface server = (ColabServerInterface) Naming.lookup(url);
        ConnectionInterface connection = server.connect(client);
        boolean correct = connection.logIn(new UserName("Chris"), "pass4");
        if (correct) {
            System.out.println("User logged in.");
            CommunityName communityName = new CommunityName("Team Awesome");
            correct = connection.logIn(communityName, "awesomePass");
            if (correct) {
                System.out.println("Logged into community.");
                ChannelName channelName = new ChannelName("Lobby");
                ClientChatChannel clientChannel =
                    new ClientChatChannel(channelName);
                //ChannelInterface serverChannel =
                    //connection.joinChannel(clientChannel, channelName);
                //System.out.println("Channle!~~!~~!~!@~bbqzomg");
                //serverChannel.add(new ChatChannelData("hi all", ));
            } else {
                System.out.println("Community login failed.");
            }
        } else {
            System.out.println("Login failed.");
        }

    }

}
