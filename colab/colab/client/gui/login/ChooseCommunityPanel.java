package colab.client.gui.login;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import colab.client.ColabClient;
import colab.common.DebugManager;
import colab.common.exception.NetworkException;
import colab.common.naming.CommunityName;

/**
 * Panel which presents a list of communities, allows the user
 * to join a community, or create a new commmunity.
 */
public class ChooseCommunityPanel extends JPanel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private JComboBox selectBox;

    private JCheckBox showAllCommCheckBox;

    private final List<ActionListener> listeners;

    private final ColabClient client;


    /** Flag to toggle whether all communities are shown
     * or just ones to which this user belongs.
     */
    private boolean showAllCommunities = false;

    /**
     * Constructs a new ChooseCommunityPanel.
     *
     * @param client the client
     */
    public ChooseCommunityPanel(final ColabClient client) {

        this.client = client;

        listeners = new ArrayList<ActionListener>();

        JLabel selectLabel = new JLabel(
                "Select the community you wish to "
                + "visit for this session: ");

        JButton joinButton = new JButton("Join Community");

        JButton newCommButton = new JButton("Create New Community");

        newCommButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                fireActionPerformed(new ActionEvent(this,
                        ActionEvent.ACTION_FIRST, "New Community"));
            }
        });

        selectBox = new JComboBox();

        joinButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                fireActionPerformed(new ActionEvent(this,
                        ActionEvent.ACTION_FIRST, "Community Chosen"));
            }
        });

        showAllCommCheckBox = new JCheckBox("Show all communities");
        showAllCommCheckBox.setSelected(showAllCommunities);
        showAllCommCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent arg0) {
                // Toggle flag to show all communities
                showAllCommunities = showAllCommCheckBox.isSelected();
                refreshCommunityNames();
            }

        });
        setLayout(new GridLayout(5, 1));

        add(selectLabel);
        add(selectBox);
        add(showAllCommCheckBox);
        add(joinButton);
        add(newCommButton);



    }


    /**
     * Refreshes the list of communities.
     */
    public void refreshCommunityNames() {

        // Get the collection of communities
        Collection<CommunityName> communities;
        try {
            if (showAllCommunities) {
                communities = client.getAllCommunityNames();
            } else {
                communities = client.getMyCommunityNames();
            }
        } catch (final NetworkException e) {
            DebugManager.network(e);
            return; // <(^.^)>
        }

        // Convert to a list of strings
        List<String> communityNames = new ArrayList<String>();
        for (final CommunityName name : communities) {
            communityNames.add(name.getValue());
        }

        // Alphabetize the list
        Collections.sort(communityNames);

        setCommunityNames(communityNames.toArray());

        //repaint();
    }

    public void setSelectedCommunity(final CommunityName communityName) {
        // Sets focus to this particular community
        selectBox.setSelectedItem(communityName.getValue());
    }

    public void setCommunityNames(final Object[] names) {
        selectBox.removeAllItems();

        for (final Object name : names) {
            selectBox.addItem(name.toString());
        }

    }

    public void addActionListener(final ActionListener l) {
        listeners.add(l);
    }

    public void fireActionPerformed(final ActionEvent e) {
        for (ActionListener l : listeners) {
            l.actionPerformed(e);
        }
    }

    public CommunityName getCurrentCommunityName() {
        String name = selectBox.getSelectedItem().toString();
        return new CommunityName(name);
    }

}
