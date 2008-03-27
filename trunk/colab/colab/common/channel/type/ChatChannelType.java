package colab.common.channel.type;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import colab.client.ClientChannel;
import colab.client.ClientChatChannel;
import colab.client.ColabClient;
import colab.client.gui.ChatChannelFrame;
import colab.client.gui.ClientChannelFrame;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;
import colab.server.channel.ServerChannel;
import colab.server.channel.ServerChatChannel;

/**
 * Chat channel type.
 *
 */
public class ChatChannelType extends ChannelType {

    public ChatChannelType() {
        super("Chat");
    }

    @Override
    public ClientChannel createClientChannel(ChannelName name)
            throws RemoteException {
        return new ClientChatChannel(name);
    }

    @Override
    public ClientChannelFrame createClientChannelFrame(
            final ColabClient client, final ClientChannel channel,
            final UserName currentUser) throws RemoteException {

        return new ChatChannelFrame(
                client, (ClientChatChannel) channel, currentUser);

    }

    @Override
    public ServerChannel createServerChannel(final ChannelName name) {
        return new ServerChatChannel(name);
    }

    @Override
    public ServerChannel createServerChannel(final ChannelName name,
            final File file) throws IOException {

        return new ServerChatChannel(name, file);

    }

}
