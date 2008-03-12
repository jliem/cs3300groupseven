package colab.client.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import colab.client.ColabClient;
import colab.common.naming.CommunityName;

class ChooseCommunityPanel extends JPanel {

    private JLabel selectLabel;
    private JButton selectButton, newCommButton;
    private JComboBox selectBox;

    private final ArrayList<ActionListener> listeners;

    public ChooseCommunityPanel(final ColabClient client) {

        listeners = new ArrayList<ActionListener>();

        selectLabel = new JLabel(
                "Select the community you wish to visit for this session: ");

        selectButton = new JButton("Select");

        newCommButton = new JButton("Create New Community");
        
        newCommButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	NewCommunityFrame frame = new NewCommunityFrame(client);
            	frame.pack();
				frame.setVisible(true);
            	frame.setPreferredSize(new Dimension(400,800));
            	//fireActionPerformed(new ActionEvent(this,
                  //      ActionEvent.ACTION_FIRST, "New Community"));
            }
        });
        
        selectBox = new JComboBox();

        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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

    public void setCommunityNames(Object[] names) {
        selectBox.removeAllItems();

        for (Object name : names)
            selectBox.addItem(name.toString());

    }

    public void addActionListener(ActionListener l) {
        listeners.add(l);
    }

    public void fireActionPerformed(ActionEvent e) {
        for (ActionListener l : listeners) {
            l.actionPerformed(e);
        }
    }

    public CommunityName getCurrentCommunityName() {
        String name = selectBox.getSelectedItem().toString();
        return new CommunityName(name);
    }

}
