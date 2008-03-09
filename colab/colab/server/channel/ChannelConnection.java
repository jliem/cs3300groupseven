package colab.server.channel;

import colab.common.identity.Identifiable;
import colab.common.remote.client.ChannelInterface;
import colab.server.connection.Connection;
import colab.server.connection.ConnectionIdentifier;

public class ChannelConnection
        implements Identifiable<ConnectionIdentifier> {

    private final Connection connection;

    private final ChannelInterface channelInterface;

    public ChannelConnection(final Connection connection,
            final ChannelInterface channelInterface) {
        this.connection = connection;
        this.channelInterface = channelInterface;
    }

    public Connection getConnection() {
        return connection;
    }

    public ChannelInterface getChannelInterface() {
        return channelInterface;
    }

    /** {@inheritDoc} */
    public ConnectionIdentifier getId() {
        return connection.getId();
    }

}
