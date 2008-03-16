package colab.client.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import colab.client.ColabClient;
import colab.common.naming.CommunityName;

class ChooseCommunityPanel extends JPanel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private JLabel selectLabel;
    private JButton selectButton, newCommButton;
    private JComboBox selectBox;

    private final List<ActionListener> listeners;

    public ChooseCommunityPanel(final ColabClient client) {

        listeners = new ArrayList<ActionListener>();

        selectLabel = new JLabel(
                "Select the community you wish to visit for this session: ");

        selectButton = new JButton("Select");

        newCommButton = new JButton("Create New Community");

        newCommButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                NewCommunityFrame frame = new NewCommunityFrame(client);
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
