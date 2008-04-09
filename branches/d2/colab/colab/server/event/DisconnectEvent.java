package colab.server.event;

import colab.server.connection.ConnectionIdentifier;

/**
 * A DisconnectEvent is an event which indicates that
 * a {@link Connection} has been dropped.
 */
public final class DisconnectEvent {

    /**
     * The id of the dropped connection.
     */
    private final ConnectionIdentifier connectionId;

    /**
     * An exception which may provide information about
     * why the connection was lost.
     */
    private final Exception cause;

    /**
     * @param connectionId the id of the connection which was lost
     * @param cause the exception which caused the disconnect
     */
    public DisconnectEvent(
            final ConnectionIdentifier connectionId,
            final Exception cause) {

        this.connectionId = connectionId;
        this.cause = cause;

    }

    /**
     * @return the id of the conneciton which was lost
     */
    public ConnectionIdentifier getConnectionId() {
        return connectionId;
    }

    /**
     * @return the exception which caused the disconnect
     */
    public Exception getCause() {
        return cause;
    }

}
