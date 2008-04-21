package colab.client.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import colab.client.ColabClient;
import colab.common.DebugManager;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.type.ChannelType;
import colab.common.channel.type.ChatChannelType;
import colab.common.channel.type.DocumentChannelType;
import colab.common.channel.type.WhiteboardChannelType;
import colab.common.exception.ChannelAlreadyExistsException;
import colab.common.exception.ConnectionDroppedException;
import colab.common.naming.ChannelName;

public class NewChannelDialog extends JDialog {
    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private final JTextField channelName;

    private final JComboBox typeCombo;

    private final ColabClient client;

    private final ChannelManagerPanel parentPanel;

    public NewChannelDialog(final ChannelManagerPanel parentPanel,
            final ColabClient client) {

        this.client = client;
        this.parentPanel = parentPanel;

        this.setLocationRelativeTo(parentPanel);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2));

        JButton createButton = new JButton("Create Channel");
        JButton cancelButton = new JButton("Cancel");

        channelName = new JTextField("");
        JLabel nameLabel = new JLabel("Channel name:");

        // Build list of possible channel types
        Vector<ChannelType> channelTypes = this.getChannelTypes();

        typeCombo = new JComboBox(channelTypes);

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
        this.setTitle("Create New Channel");

        pack();

        this.setModal(true);
    }


    /**
     * Get a list of possible channel types.
     *
     * @return a Vector of channel types
     */
    private Vector<ChannelType> getChannelTypes() {
        Vector<ChannelType> channelTypes = new Vector<ChannelType>();

        channelTypes.add(new ChatChannelType());
        channelTypes.add(new DocumentChannelType());
        channelTypes.add(new WhiteboardChannelType());

        return channelTypes;
    }

    /**
     * Show an error box.
     * @param message the message
     * @param title the title
     */
    private void showErrorBox(final String message, final String title) {
        JOptionPane.showMessageDialog(
                this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Create a new channel.
     */
    private void createChannel() {

        if (channelName.getText().length() > 0
                && typeCombo.getSelectedIndex() > -1 
                	&& isAllowableName(channelName.getText())) {

            ChannelName name = new ChannelName(channelName.getText());
            ChannelType type = (ChannelType)(typeCombo.getSelectedItem());

            ChannelDescriptor desc = new ChannelDescriptor(name, type);

            // Check whether this channel already exists
            Vector<ChannelDescriptor> channels = client.getChannels();
            if (channels.contains(desc)) {
                showErrorBox(
                        "A channel with that name already exists. "
                        + "Please enter a unique name for the channel.",
                        "Duplicate Channel");
            } else {

                try {
                    client.createChannel(desc);
                } catch (final ChannelAlreadyExistsException e) {
                    DebugManager.exception(e);
                } catch (final ConnectionDroppedException e) {
                    DebugManager.exception(e);
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

    private boolean isAllowableName(String text) {
		// TODO Auto-generated method stub
    	
    	for(int i = 0; i < text.length(); i++){
    		if(text.charAt(i) == '?' || text.charAt(i) == 92 || text.charAt(i) == '/'
    				|| text.charAt(i) == '%' || text.charAt(i) == '*' || text.charAt(i) == ':'
    					|| text.charAt(i) == '|' || text.charAt(i) == '"' || text.charAt(i) == '<'
    						|| text.charAt(i) == '>' || text.charAt(i) == '.' || text.charAt(i) == 39)
    							showErrorBox("Only the characters A-Z, numeric characters, and underscores are allowed for" +
    									"channel names", "Invalid character" );
    							return false;    						
    	}
		return true;
	}


	/**
     * Closes this window.
     */
    private void closeWindow() {
        this.dispose();
    }
}
