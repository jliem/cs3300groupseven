package colab.common.channel.type;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import colab.client.ClientChannel;
import colab.client.ClientDocumentChannel;
import colab.client.ColabClient;
import colab.client.gui.ClientChannelFrame;
import colab.client.gui.document.DocumentChannelFrame;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;
import colab.server.channel.ServerChannel;
import colab.server.channel.ServerDocumentChannel;

/**
 * Document channel type.
 *
 */
public class DocumentChannelType extends ChannelType {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    public DocumentChannelType() {
        super("Document");
    }

    @Override
    public ClientChannel createClientChannel(final ChannelName name)
            throws RemoteException {
        return new ClientDocumentChannel(name);
    }

    @Override
    public ClientChannelFrame createClientChannelFrame(
            final ColabClient client, final ClientChannel channel,
            final UserName currentUser) throws RemoteException {

        return new DocumentChannelFrame(
                client, (ClientDocumentChannel)channel,
                currentUser);

    }

    @Override
    public ServerChannel createServerChannel(final ChannelName name) {
        return new ServerDocumentChannel(name);
    }

    @Override
    public ServerChannel createServerChannel(final ChannelName name,
            final File file) throws IOException {

        return new ServerDocumentChannel(name, file);

    }


}
