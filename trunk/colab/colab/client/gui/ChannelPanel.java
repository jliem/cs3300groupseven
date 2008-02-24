package colab.client.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import colab.common.channel.ChannelDescriptor;

public class ChannelPanel extends JPanel {

    private final ArrayList<ActionListener> listeners;

    private final LinkedList<ChannelDescriptor> pendingJoins;

    private final Vector<ChannelDescriptor> channels;

    private final JList channelList;

    public ChannelPanel() {
        listeners = new ArrayList<ActionListener>();

        pendingJoins = new LinkedList<ChannelDescriptor>();
        channels = new Vector<ChannelDescriptor>();

        channelList = new JList(channels);

        channelList.setPreferredSize(new Dimension(100, 230));
        setPreferredSize(new Dimension(100, 275));

        channelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        channelList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    // adds the currently selected channel descriptor to our
                    // pending join channel list
                    pendingJoins.add(channels.elementAt(channelList
                            .getLeadSelectionIndex()));
                    fireActionPerformed(new ActionEvent(this,
                            ActionEvent.ACTION_FIRST, "Joined"));
                }
            }
        });

        add(channelList);
    }

    public void addChannel(ChannelDescriptor chan) {
        channels.add(chan);
    }

    public void removeChannel(ChannelDescriptor chan) {
        channels.remove(chan);
    }

    public ChannelDescriptor popJoinedChannel() {
        if (pendingJoins.size() > 0) {
            return pendingJoins.removeFirst();
        }
        return null;
    }

    public void addActionListener(ActionListener l) {
        listeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        listeners.remove(l);
    }

    public void fireActionPerformed(ActionEvent e) {
        for (ActionListener l : listeners) {
            l.actionPerformed(e);
        }
    }
}