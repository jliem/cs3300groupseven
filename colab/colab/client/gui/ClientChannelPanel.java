package colab.client.gui;

import javax.swing.JPanel;

/**
 * Parent class for all client-side channel panels. The child
 * class is responsible for adding the userListPanel to itself.
 */
abstract class ClientChannelPanel extends JPanel {

    /**
     * A panel which contains a list of active users in the channel.
     */
    private final UserListPanel userListPanel;

    /**
     * Constructs a new ClientChannelPanel.
     */
    public ClientChannelPanel() {

        userListPanel = new UserListPanel();

    }

    /**
     * @return a panel which contains a list of active users in the channel
     */
    public final UserListPanel getUserListPanel() {

        return userListPanel;

    }

}
