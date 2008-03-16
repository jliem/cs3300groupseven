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
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import colab.common.channel.ChannelDescriptor;

final class ChannelManagerPanel extends JPanel {

    /** Serialization verson number. */
    public static final long serialVersionUID = 1L;

    private final ArrayList<ActionListener> listeners;

    private final LinkedList<ChannelDescriptor> pendingJoins;

    private final Vector<ChannelDescriptor> channels;

    private final JList channelList;


    public ChannelManagerPanel(Vector <ChannelDescriptor> channelListModel) {

        listeners = new ArrayList<ActionListener>();

        pendingJoins = new LinkedList<ChannelDescriptor>();
        channels = channelListModel;

        channelList = new JList(channels);


       //menu.add(changeCommunityItem);

        channelList.setPreferredSize(new Dimension(100, 230));
        JScrollPane scrollChan = new JScrollPane(channelList);
        scrollChan.setPreferredSize(new Dimension(110, 240));
        setPreferredSize(new Dimension(115, 275));

        channelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        channelList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() > 1 && !channelList.isSelectionEmpty()) {
                    // adds the currently selected channel descriptor to our
                    // pending join channel list
                    pendingJoins.add(channels.elementAt(channelList
                            .getLeadSelectionIndex()));
                    fireActionPerformed(new ActionEvent(this,
                            ActionEvent.ACTION_FIRST, "Joined"));
                }
            }
        });

        ActionListener al = new ActionListener() {

            public void actionPerformed(final ActionEvent e) {

                    fireActionPerformed(new ActionEvent(this,
                            ActionEvent.ACTION_FIRST, "Change!"));
                }


        };

        add(scrollChan);
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

}
