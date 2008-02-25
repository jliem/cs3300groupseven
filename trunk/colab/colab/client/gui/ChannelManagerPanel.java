package colab.client.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import colab.common.channel.ChannelDescriptor;

class ChannelManagerPanel extends JPanel {

    private final ArrayList<ActionListener> listeners;

    private final LinkedList<ChannelDescriptor> pendingJoins;

    private final Vector<ChannelDescriptor> channels;

    private final JList channelList;
    
    private final JMenuBar menuBar;
    
    private final JMenu menu;
    
    private final JMenuItem logoutItem, changeCommunityItem;
    

    public ChannelManagerPanel(Vector <ChannelDescriptor> channelListModel) {

        listeners = new ArrayList<ActionListener>();

        pendingJoins = new LinkedList<ChannelDescriptor>();
        channels = channelListModel;

        channelList = new JList(channels);
        
        menuBar = new JMenuBar();
        menu = new JMenu("File");
        menuBar.add(menu);
        logoutItem = new JMenuItem("Logout");
        changeCommunityItem = new JMenuItem("Change Community");
              
       menu.add(changeCommunityItem);
       menu.add(logoutItem);
        
        channelList.setPreferredSize(new Dimension(100, 230));
        JScrollPane scrollChan = new JScrollPane(channelList);
        scrollChan.setPreferredSize(new Dimension(110, 240));
        setPreferredSize(new Dimension(115, 275));

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
        
        ActionListener al = new ActionListener() {

            public void actionPerformed(final ActionEvent e) {

                if (e.getSource() == logoutItem) {
                	fireActionPerformed(new ActionEvent(this,
                            ActionEvent.ACTION_FIRST, "Logout!"));
                }
                
                else if (e.getSource() == changeCommunityItem) {
                	fireActionPerformed(new ActionEvent(this,
                            ActionEvent.ACTION_FIRST, "Change!"));
                }

            }
        };
        
        logoutItem.addActionListener(al);
        changeCommunityItem.addActionListener(al);

        add(scrollChan);
    }

    public ChannelDescriptor dequeueJoinedChannel() {
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
    
    public JMenuBar getMenuBar(){
    	return menuBar;
    }

}
