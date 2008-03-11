package colab.server.event;

/**
 * An object which can be notified when a connection is dropped.
 */
public interface DisconnectListener {

    /**
     * Called when the connection is dropped.
     *
     * @param e the disconnect event
     */
    void handleDisconnect(DisconnectEvent e);

}
