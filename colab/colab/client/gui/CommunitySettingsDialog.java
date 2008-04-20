package colab.client.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import colab.client.ColabClient;
import colab.common.DebugManager;
import colab.common.exception.CommunityDoesNotExistException;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import colab.server.user.User;

public class CommunitySettingsDialog extends JDialog {

    /** Scroll pane for the user list. */
    private final JScrollPane scroll;

    /** The displayable list of users. */
    private final JList userList;

    /** List model. */
    private final DefaultListModel listModel;

    /** The sorted list of users. */
    private final TreeSet<UserName> users;

    private final ColabClient client;

    private final CommunityName communityName;

    private final UserName currentUser;

    public CommunitySettingsDialog(ColabClient client,
            CommunityName communityName,
            UserName currentUser) {

        this.client = client;
        this.communityName = communityName;
        this.currentUser = currentUser;

        listModel = new DefaultListModel();

        users = new TreeSet<UserName>();

        userList = new JList(listModel);

        userList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        userList.setListData(getMembers());

        scroll = new JScrollPane(userList);

        downloadMembers();

        JButton selectAllButton = new JButton("Select All");
        selectAllButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                userList.setSelectionInterval(0, users.size()-1);
            }

        });

        JButton deselectAllButton = new JButton("Deselect All");
        deselectAllButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                userList.clearSelection();
            }

        });

        JButton removeButton = new JButton("Remove Selected");
        removeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                Object[] array = userList.getSelectedValues();
                for (Object o : array) {
                    UserName user = (UserName)o;

                    try {
                        CommunitySettingsDialog.this.client.removeMember(user,
                                CommunitySettingsDialog.this.communityName);

                    } catch (CommunityDoesNotExistException e) {
                        DebugManager.exception(e);
                    } catch (RemoteException e) {
                        DebugManager.remote(e);
                    }
                }

                // Re-download the list
                downloadMembers();

            }

        });

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(selectAllButton);
        buttonPanel.add(deselectAllButton);
        buttonPanel.add(removeButton);

        this.setLayout(new BorderLayout());
        this.add(scroll, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        this.setModal(true);
    }

    /**
     * Refresh the list displayed in the UI.
     */
    public void refreshMembersGUI() {
        userList.setListData(getMembers());
    }

    /**
     * Downloads all users connected to a channel and adds them to the user list
     * without triggering a join event for each user.
     *
     * @param client the client
     * @param channel the channel you are joining
     * @throws RemoteException if a RemoteException occurs.
     * @throws CommunityDoesNotExistException
     */
    private void downloadMembers() {

        Collection<UserName> users;

        try {
            users = client.getMembers(communityName);

            // Clear existing list
            this.users.clear();

            // Don't add ourselves to the list
            users.remove(currentUser);

            this.addMembers(users);

        } catch (CommunityDoesNotExistException e) {
            DebugManager.exception(e);
        } catch (RemoteException e) {
            DebugManager.remote(e);
        }

        // Since we just got a new list, update GUI
        refreshMembersGUI();

    }

    /**
     * Adds users without triggering a userJoined event.
     *
     * @param users the users to add
     */
    private void addMembers(final Collection<UserName> users) {

        if (users != null) {
            this.users.addAll(users);
        }

        refreshMembersGUI();

    }

    /**
     * Returns the list of users sorted in alphabetical order.
     * @return the sorted user list
     */
    private UserName[] getMembers() {
        UserName[] arr = users.toArray(new UserName[0]);
        // toArray should guarantee the data is sorted by TreeSet
        return arr;
    }

}
