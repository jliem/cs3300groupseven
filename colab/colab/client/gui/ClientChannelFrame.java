package colab.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import javax.swing.JFrame;

import colab.client.ClientChannel;
import colab.client.ColabClient;
import colab.common.DebugManager;

/**
 * Parent class for all client-side channel frames. This class
 * will set up the list of users and add the main panel.
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
    protected final ClientChannel channel;


    /**
     * A panel which contains a list of active users in the channel.
     */
    protected final UserListPanel userListPanel;

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
        this.channel = clientChannel;
        this.clientChannelPanel = clientChannelPanel;

        this.userListPanel = new UserListPanel();

        if (clientChannelPanel != null && clientChannel != null) {
            // Set up the list of users
            clientChannel.addUserListener(userListPanel);
            try {
                // Download list of current users
                userListPanel.downloadActiveUsers(client,
                        clientChannel);
            } catch (RemoteException ex) {
                // TODO: Handle remote exception
                if (DebugManager.EXCEPTIONS) {
                    ex.printStackTrace();
                }
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

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setSize(new Dimension(320, 300));

        this.setLayout(new BorderLayout());

        add(clientChannelPanel, BorderLayout.CENTER);
        add(userListPanel, BorderLayout.EAST);

    }

    /**
     * Perform cleanup before exiting this window.
     * @throws RemoteException
     */
    public void exit() {
        // Before closing window, try to exit the channel
        try {
            leaveChannel();
        } catch (RemoteException re) {
            if (DebugManager.EXIT) {
                re.printStackTrace();
            }
        }

        setVisible(false);
        dispose();
    }

    /**
     * Returns the client channel.
     * @return the client channel
     */
    public final ClientChannel getChannel() {
        return channel;
    }

    /**
     * @return a panel which contains a list of active users in the channel
     */
    public final UserListPanel getUserListPanel() {
        return userListPanel;
    }

    /**
     * Indicates that this client is leaving this channel.
     * @throws RemoteException
     */
    private void leaveChannel() throws RemoteException  {

        if (channel != null) {
            client.leaveChannel(channel.getChannelDescriptor());
        }
    }
}
