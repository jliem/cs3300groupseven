package colab.server.event;

import colab.server.connection.ConnectionIdentifier;


public class DisconnectEvent {

    private final ConnectionIdentifier connectionId;

    private final Exception cause;

    public DisconnectEvent(
            final ConnectionIdentifier connectionId,
            final Exception cause) {

        this.connectionId = connectionId;
        this.cause = cause;

    }

    public ConnectionIdentifier getConnectionId() {
        return connectionId;
    }

    public Exception getCause() {
        return cause;
    }

}
