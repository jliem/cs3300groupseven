package colab.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import colab.client.ClientChannel;
import colab.client.ColabClient;
import colab.common.event.UserJoinedEvent;
import colab.common.event.UserLeftEvent;
import colab.common.event.UserListener;
import colab.common.naming.UserName;

/**
 * A panel to display all the users in this Channel. Comes wrapped in its own
 * scrollable pane. To use this panel, a class must add an instance of
 * UserListPanel to the channel using the addUserListener method, then call
 * downloadActiveUsers in order to get all users currently connected. The list
 * will update itself when users join/leave.
 *
 * Example usage in ChatChannelFrame:
 *
 *   channel.addUserListener(chatPanel.getUserListPanel());
 *   try {
 *       // Download list of current users
 *       chatPanel.getUserListPanel().downloadActiveUsers(client, channel);
 *   } catch (final RemoteException ex) {
 *       // Handle remote exception
 *       ex.printStackTrace();
 *   }
 *
 */
public final class UserListPanel extends JPanel implements UserListener {

    /**
     * Serial version ID.
     */
    private static final long serialVersionUID = 1L;

    /** Scroll pane for the user list. */
    private final JScrollPane scroll;

    /** The displayable list of users. */
    private final JList userList;

    /** List model. */
    private final DefaultListModel listModel;

    /** The sorted list of users. */
    private final TreeSet<UserName> users;

    /**
     * Create a new UserListPanel.
     */
    public UserListPanel() {
        listModel = new DefaultListModel();

        users = new TreeSet<UserName>();

        userList = new JList(listModel);
        // Set this to only allow selection of one element at a time
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        userList.setListData(getUsers());

        scroll = new JScrollPane(userList);

        this.setLayout(new BorderLayout());
        this.add(scroll, BorderLayout.CENTER);

        this.setPreferredSize(new Dimension(100, 10));
    }

    /**
     * Downloads all users connected to a channel and adds them to the user list
     * without triggering a join event for each user.
     *
     * @param client the client
     * @param channel the channel you are joining
     * @throws RemoteException if a RemoteException occurs.
     */
    public void downloadActiveUsers(final ColabClient client,
            final ClientChannel channel) throws RemoteException {

        Collection<UserName> users = client.getActiveUsers(channel.getId());

        this.addUsers(users);

    }

    /**
     * Adds users without triggering a userJoined event.
     *
     * @param users the users to add
     */
    private void addUsers(final Collection<UserName> users) {

        if (users != null)
            this.users.addAll(users);

        refreshUsers();

    }

    /**
     * Adds a new user to the userlist.
     *
     * @param joinEvent the user to add
     */
    public void handleUserEvent(final UserJoinedEvent joinEvent) {

        users.add(joinEvent.getUserName());
        this.refreshUsers();

    }

    /**
     * Removes a user from the list.
     *
     * @param leaveEvent the user to remove
     */
    public void handleUserEvent(final UserLeftEvent leaveEvent) {
        users.remove(leaveEvent.getUserName());
        this.refreshUsers();
    }

    /**
     * Returns the list of users sorted in alphabetical order.
     * @return the sorted user list
     */
    public UserName[] getUsers() {
        UserName[] arr = users.toArray(new UserName[0]);
        // toArray should guarantee the data is sorted by TreeSet
        return arr;
    }

    /**
     * Refresh the list displayed in the UI.
     */
    public void refreshUsers() {
        userList.setListData(getUsers());
    }

    /**
     * Checks whether a userName is contained in this user list.
     * @param userName the userName to find
     * @return true if the userName is in the list, false otherwise
     */
    public boolean contains(final UserName userName) {
        return users.contains(userName);
    }

}
