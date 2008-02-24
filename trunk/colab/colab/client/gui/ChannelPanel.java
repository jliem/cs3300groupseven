package colab.client.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import colab.common.channel.ChannelDescriptor;

public class ChannelPanel extends JPanel {
  
  private final LinkedList<ChannelDescriptor> pendingJoins;
  private final ArrayList<ChannelDescriptor> channels;
  
  private final JList channelList ;
  
  public ChannelPanel()
  {
    pendingJoins = new LinkedList<ChannelDescriptor>();
    channels = new ArrayList<ChannelDescriptor>();
    
    channelList = new JList();
    JScrollPane listScroll = new JScrollPane(channelList);
    
    listScroll.setPreferredSize(new Dimension(100, 230));
    setPreferredSize(new Dimension(100, 275));
    
    add(listScroll);
  }
  
  public void addChannel(ChannelDescriptor chan)
  {
      channels.add(chan);
      
  }
  
//  public void addChannel(ChannelDescriptor chan)
//  {
//      
//  }
  
  public static void main(final String args[]){
      JFrame f = new JFrame();
      f.setLayout(new FlowLayout());
      ChannelPanel chan = new ChannelPanel();
      ChatPanel chat = new ChatPanel("test");
      f.add(chan);
      f.add(chat);
      f.setSize(425, 275);
      f.setVisible(true);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}