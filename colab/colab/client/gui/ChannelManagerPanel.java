package colab.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import colab.client.ColabClient;
import colab.common.channel.ChannelDescriptor;

final class ChannelManagerPanel extends JPanel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private final ArrayList<ActionListener> listeners;

    private final LinkedList<ChannelDescriptor> pendingJoins;

    private ChannelDescriptor[] channels;

    private final JList channelList;

    private final ColabClient client;

    public ChannelManagerPanel(ColabClient client) {

        this.client = client;

        this.setLayout(new BorderLayout());

        listeners = new ArrayList<ActionListener>();

        pendingJoins = new LinkedList<ChannelDescriptor>();

        channels = getSortedChannels();

        channelList = new JList(channels);

        channelList.setPreferredSize(new Dimension(100, 230));
        JScrollPane scrollChan = new JScrollPane(channelList);
        scrollChan.setPreferredSize(new Dimension(110, 240));

        channelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        channelList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() > 1) {
                    joinSelectedChannel();
                }
            }
        });

        add(scrollChan, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton joinButton = new JButton("Join Channel");
        JButton createChannelButton = new JButton("Create New Channel");

        joinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                joinSelectedChannel();
            }
        });

        createChannelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                handleCreateNewChannel();
            }
        });

        buttonPanel.add(joinButton);
        buttonPanel.add(createChannelButton);

        add(buttonPanel, BorderLayout.SOUTH);


        setPreferredSize(new Dimension(400, 400));

    }

    /**
     * Creates GUI to prompt user when creating
     * a new channel.
     */
    public void handleCreateNewChannel() {
        NewChannelFrame ncf = new NewChannelFrame(this, client);

        ncf.pack();
        ncf.setVisible(true);
    }

    /**
     * Joins the selected channel in the list box.
     */
    public void joinSelectedChannel() {
        if (!channelList.isSelectionEmpty()) {
            // adds the currently selected channel descriptor to our
            // pending join channel list

            pendingJoins.add(channels[channelList
                    .getLeadSelectionIndex()]);
            fireActionPerformed(new ActionEvent(this,
                    ActionEvent.ACTION_FIRST, "Joined"));
        } else {
            showErrorDialog("Please select a channel from the list.",
            "No Channel Selected");
        }
    }

    /**
     * Pre-selects a specific channel.
     *
     * @param desc the channel descriptor
     */
    public void setSelectedChannel(ChannelDescriptor desc) {
        channelList.setSelectedValue(desc, true);
    }

    /**
     * Retrieves the latest list of channels from the client
     * and refreshes the UI.
     */
    public void refreshChannels() {
        channels = getSortedChannels();
        channelList.setListData(channels);
    }

    public ChannelDescriptor dequeueJoinedChannel() {
        if (pendingJoins.size() > 0) {
            return pendingJoins.removeFirst();
        }
        return null;
    }


    public void addActionListener(final ActionListener listener) {
        listeners.add(listener);
    }

    public void removeActionListener(final ActionListener listener) {
        listeners.remove(listener);
    }

    public void fireActionPerformed(ActionEvent event) {
        for (final ActionListener listener : listeners) {
            listener.actionPerformed(event);
        }
    }

    public void showInfoDialog(String message, String title) {
        JOptionPane.showMessageDialog(this,
                message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorDialog(String message, String title) {
        JOptionPane.showMessageDialog(this,
                message, title, JOptionPane.ERROR_MESSAGE);
    }

    private ChannelDescriptor[] getSortedChannels() {
        Vector<ChannelDescriptor> vector = client.getChannels();
        ChannelDescriptor[] arr = vector.toArray(new ChannelDescriptor[0]);
        Arrays.sort(arr);
        return arr;
    }
}
