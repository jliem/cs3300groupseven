package colab.client.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import colab.client.ColabClient;
import colab.common.DebugManager;
import colab.common.exception.NetworkException;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;

class ChooseCommunityPanel extends JPanel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private JLabel selectLabel;
    private JButton selectButton, newCommButton;
    private JComboBox selectBox;

    private final List<ActionListener> listeners;

    private final ColabClient client;

    public ChooseCommunityPanel(final ColabClient client) {

        this.client = client;

        listeners = new ArrayList<ActionListener>();

        selectLabel = new JLabel(
                "Select the community you wish to visit for this session: ");

        selectButton = new JButton("Select");

        newCommButton = new JButton("Create New Community");

        newCommButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                NewCommunityFrame frame = new NewCommunityFrame(getThis(), client);
                frame.pack();
                frame.setVisible(true);
                frame.setPreferredSize(new Dimension(400, 800));
                //fireActionPerformed(new ActionEvent(this,
                  //      ActionEvent.ACTION_FIRST, "New Community"));
            }
        });

        selectBox = new JComboBox();

        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                fireActionPerformed(new ActionEvent(this,
                        ActionEvent.ACTION_FIRST, "Community Chosen"));
            }
        });

        setLayout(new GridLayout(4, 1));

        add(selectLabel);
        add(selectBox);
        add(selectButton);
        add(newCommButton);

    }

    /**
     * Refreshes the list of communities.
     */
    public void refreshCommunityNames() {

        // Get the collection of my communities
        Collection<CommunityName> myCommunities;
        try {
            myCommunities = client.getMyCommunityNames();
        } catch (final NetworkException e) {
            if (DebugManager.NETWORK) {
                e.printStackTrace();
            }

            return; // <(^.^)>
        }

        // Convert to a list of strings
        List<String> myCommunityNames = new ArrayList<String>();
        for (final CommunityName name : myCommunities) {
            myCommunityNames.add(name.getValue());
        }

        // Alphabetize the list
        Collections.sort(myCommunityNames);

        setCommunityNames(myCommunityNames.toArray());

        repaint();
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


    /**
     * Lame hack to get access to the instance of ChooseCommunityPanel
     * from the anonymous inner class.
     * @return a reference to this
     */
    private ChooseCommunityPanel getThis() {
        return this;
    }
}
