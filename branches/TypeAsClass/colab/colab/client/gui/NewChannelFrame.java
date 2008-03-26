package colab.client.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import colab.client.ColabClient;
import colab.common.DebugManager;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.type.ChannelType;
import colab.common.naming.ChannelName;

public class NewChannelFrame extends JFrame {
    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private final JTextField channelName;

    private final JComboBox typeCombo;

    private final ColabClient client;

    private final ChannelManagerPanel parentPanel;

    public NewChannelFrame(final ChannelManagerPanel parentPanel,
            final ColabClient client) {

        this.client = client;
        this.parentPanel = parentPanel;

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2));

        JButton createButton = new JButton("Create Channel");
        JButton cancelButton = new JButton("Cancel");

        channelName = new JTextField("");
        JLabel nameLabel = new JLabel("Channel name:");
        typeCombo = new JComboBox(ChannelType.getChannelTypes());

        JLabel typeLabel = new JLabel("Channel type:");

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                createChannel();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                closeWindow();
            }
        });

        //this.setPreferredSize(new Dimension(800,400));


        inputPanel.add(nameLabel);
        inputPanel.add(channelName);

        inputPanel.add(typeLabel);
        inputPanel.add(typeCombo);

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);

        this.getContentPane().setLayout(new BorderLayout());
        this.add(inputPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

    }


    private void showErrorBox(final String message, final String title) {
        JOptionPane.showMessageDialog(
                this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void createChannel() {

        if (channelName.getText().length() > 0
                && typeCombo.getSelectedIndex() > -1) {

            ChannelName name = new ChannelName(channelName.getText());
            ChannelType type = (ChannelType)(typeCombo.getSelectedItem());

            ChannelDescriptor desc = new ChannelDescriptor(name, type);

            // Check whether this channel already exists
            Vector<ChannelDescriptor> channels = client.getChannels();
            if (channels.contains(desc)) {
                showErrorBox(
                        "A channel with that name and type already exists.",
                        "Duplicate Channel");
            } else {

                try {
                    client.createChannel(desc);
                } catch (RemoteException re) {
                    if (DebugManager.EXCEPTIONS) {
                        re.printStackTrace();
                    }
                }

                // Show updated list
                parentPanel.refreshChannels();

                // Select this channel automatically
                parentPanel.setSelectedChannel(desc);

                // Destroy this window
                this.closeWindow();
            }

        }
    }

    /**
     * Closes this window.
     */
    private void closeWindow() {
        this.dispose();
    }
}
