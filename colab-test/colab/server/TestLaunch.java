package colab.server;

import java.rmi.Naming;

import colab.client.ColabClient;
import colab.common.naming.ChannelName;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import colab.common.remote.server.ColabServerRemote;
import colab.common.remote.server.ConnectionRemote;

public class TestLaunch {

    public static void main(String[] args) throws Exception {

        ColabServer.main(new String[]{});

        Thread.sleep(1000L);

        ColabClient client = new ColabClient();

        String url = "//localhost:" + 9040 + "/COLAB_SERVER";

        ColabServerRemote server = (ColabServerRemote) Naming.lookup(url);
        ConnectionRemote connection = server.connect(client);
        connection.logIn(new UserName("Chris"), "pass4".toCharArray());

        System.out.println("User logged in.");

        CommunityName communityName = new CommunityName("Team Awesome");
        connection.logIn(communityName, "awesomePass".toCharArray());

        System.out.println("Logged into community.");

        ChannelName channelName = new ChannelName("Lobby");
        //ClientChatChannel clientChannel =
        //    new ClientChatChannel(channelName);

        //ChannelInterface serverChannel =
            //connection.joinChannel(clientChannel, channelName);
        //System.out.println("Channle!~~!~~!~!@~bbqzomg");
        //serverChannel.add(new ChatChannelData("hi all", ));

    }

}
