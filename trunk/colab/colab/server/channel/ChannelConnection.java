package colab.server.channel;

import colab.common.identity.Identifiable;
import colab.common.remote.client.ChannelInterface;
import colab.server.connection.Connection;
import colab.server.connection.ConnectionIdentifier;

/**
 * ChannelConnection is a wrapper for a remote {@link ChannelInterface}
 * which associates the channel interface with the {@link Connection}
 * on which it was created.
 */
public final class ChannelConnection
        implements Identifiable<ConnectionIdentifier> {

    /** The connection which created the channel interface. */
    private final Connection connection;

    /** The channel interface. */
    private final ChannelInterface channelInterface;

    /**
     * @param connection the connection which created the channel interface
     * @param channelInterface the channel interface
     */
    public ChannelConnection(final Connection connection,
            final ChannelInterface channelInterface) {
        this.connection = connection;
        this.channelInterface = channelInterface;
    }

    /**
     * @return the connection which created the channel interface
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @return the channel interface
     */
    public ChannelInterface getChannelInterface() {
        return channelInterface;
    }

    /** {@inheritDoc} */
    public ConnectionIdentifier getId() {
        return connection.getId();
    }

}
