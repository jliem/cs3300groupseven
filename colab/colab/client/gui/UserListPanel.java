package colab.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import colab.client.event.UserJoinedEvent;
import colab.client.event.UserLeftEvent;
import colab.client.event.UserListener;
import colab.common.naming.UserName;

/**
 * A panel to display all the users in this Channel. Comes wrapped in its own
 * scrollable pane.
 *
 */
public final class UserListPanel extends JPanel implements UserListener {

    /**
     *
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

        this.setPreferredSize(new Dimension(120, 10));
    }

    /**
     * Adds a new user to the userlist.
     *
     * @param userName the user to add
     */
    public void userJoined(UserJoinedEvent ue) {
        System.out.println(ue.getUserName() + " has joined!");
        users.add(ue.getUserName());
        this.updateUsers();
    }

    /**
     * Removes a user from the list.
     * @param userName the user to remove
     */
    public void userLeft(UserLeftEvent ue) {
        System.out.println(ue.getUserName() + " has left!");
        users.remove(ue.getUserName());
        this.updateUsers();
    }

    /**
     * Returns the list of users sorted in alphabetical order.
     * @return the sorted user list
     */
    public UserName[] getUsers() {
        UserName[] arr = users.toArray(new UserName[0]);
        // toArray should guarantee the data is sorted by TreeSet
        //Arrays.sort(arr);
        return arr;
    }

    /**
     * Refresh the list displayed in the UI.
     */
    public void updateUsers() {
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
