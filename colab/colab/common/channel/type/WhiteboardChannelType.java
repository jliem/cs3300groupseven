package colab.common.channel.type;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import colab.client.ClientChannel;
import colab.client.ClientWhiteboardChannel;
import colab.client.ColabClient;
import colab.client.gui.ClientChannelFrame;
import colab.client.gui.whiteboard.WhiteboardChannelFrame;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;
import colab.server.channel.ServerChannel;
import colab.server.channel.ServerWhiteboardChannel;

/**
 * Whiteboard channel type.
 *
 */
public class WhiteboardChannelType extends ChannelType {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    public WhiteboardChannelType() {
        super("Whiteboard");
    }

    @Override
    public ClientChannel createClientChannel(final ChannelName name)
            throws RemoteException {
        return new ClientWhiteboardChannel(name);
    }

    @Override
    public ClientChannelFrame createClientChannelFrame(
            final ColabClient client, final ClientChannel channel,
            final UserName currentUser) throws RemoteException {

        return new WhiteboardChannelFrame(
                client, (ClientWhiteboardChannel)channel,
                currentUser);

    }

    @Override
    public ServerChannel createServerChannel(final ChannelName name) {
        return new ServerWhiteboardChannel(name);
    }

    @Override
    public ServerChannel createServerChannel(final ChannelName name,
            final File file) throws IOException {

        return new ServerWhiteboardChannel(name, file);

    }


}
