package colab.client.gui;

import javax.swing.JPanel;

public abstract class ClientChannelPanel extends JPanel {

    protected final UserListPanel userListPanel;

    public ClientChannelPanel() {

        userListPanel = new UserListPanel();
    }

    public UserListPanel getUserListPanel() {
        return userListPanel;
    }

}
