package colab.client.gui;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import javax.swing.JFrame;

import colab.client.ClientChannel;
import colab.client.ColabClient;
import colab.common.DebugManager;

/**
 * Parent class for all client-side channel frames.
 */
abstract class ClientChannelFrame extends JFrame {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /** The client. */
    protected final ColabClient client;

    /** The main panel associated with this frame. */
    protected final ClientChannelPanel clientChannelPanel;

    /** The channel associated with this frame. */
    protected final ClientChannel clientChannel;

    /**
     * Creates a new ClientChannelFrame.
     * @param client the client for this frame
     * @param clientChannel the client channel
     * @param clientChannelPanel the client channel panel
     */
    public ClientChannelFrame(final ColabClient client,
            final ClientChannel clientChannel,
            final ClientChannelPanel clientChannelPanel) {

        this.client = client;
        this.clientChannel = clientChannel;
        this.clientChannelPanel = clientChannelPanel;

        if (clientChannelPanel != null && clientChannel != null) {
            // Set up the list of users
            clientChannel.addUserListener(clientChannelPanel.getUserListPanel());
            try {
                // Download list of current users
                clientChannelPanel.getUserListPanel().downloadActiveUsers(client,
                        clientChannel);
            } catch (RemoteException ex) {
                // TODO: Handle remote exception
                ex.printStackTrace();
            }
        }

        // If this frame is closed, exit the channel and clean up
        addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                exit();
            }
        });

        // Set default title and size
        if (clientChannel != null) {
            setTitle(clientChannel.getId().toString());
        } else {
            setTitle("(No Channel)");
        }

        setSize(new Dimension(320, 300));
    }

    /**
     * Perform cleanup before exiting this window.
     */
    public void exit() {
        // Before closing window, try to exit the channel
        leaveChannel();

        setVisible(false);
        dispose();
    }

    /**
     * Indicates that this client is leaving this channel.
     */
    private void leaveChannel()  {

        if (clientChannel != null) {
            try {
                client.leaveChannel(clientChannel.getChannelDescriptor());
            } catch (Exception e) {
                // TODO: Handle this? Might not be necessary
                if (DebugManager.EXIT_EXCEPTIONS)
                    e.printStackTrace();
            }
        }
    }
}
