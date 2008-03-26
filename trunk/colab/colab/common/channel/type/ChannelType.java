package colab.common.channel.type;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import colab.client.ClientChannel;
import colab.client.ColabClient;
import colab.client.gui.ClientChannelFrame;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;
import colab.server.channel.ServerChannel;

/**
 * Parent class to represent a channel type.
 */
public abstract class ChannelType implements Serializable {

    /** Human-readable representation */
    private final String string;

    /**
     * Constructor for ChannelType.
     *
     * @param string the human-readable representation of this
     * ChannelType
     */
    public ChannelType(final String string) {
        this.string = string;
    }

    /**
     * Creates a new ClientChannel object with the given name.
     *
     * @param name the channel name
     * @return a reference to a new ClientChannel subclass object
     */
    public abstract ClientChannel createClientChannel(
            final ChannelName name) throws RemoteException;

    /**
     * Creates a new ClientChannelFrame.
     *
     * @param client the client
     * @param channel the channel
     * @param currentUser the user currently logged in
     * @return a reference to a new ClientChannelFrame subclass object
     */
    public abstract ClientChannelFrame createClientChannelFrame(
            final ColabClient client, final ClientChannel channel,
            final UserName currentUser) throws RemoteException;

    /**
     * Creates a new ServerChannel.
     *
     * @param name the name of the channel
     * @return a reference to a new ServerChannel subclass object
     */
    public abstract ServerChannel createServerChannel(final ChannelName name);

    /**
     * Creates a new ServerChannel.
     *
     * @param name the name of the channel
     * @param file the file to create this channel from
     * @return a reference to a new ServerChannel subclass object
     */
    public abstract ServerChannel createServerChannel(final ChannelName name,
            final File file) throws IOException;

    public String toString() {
        return string;
    }
}
