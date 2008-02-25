package colab.client.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JFrame;

import colab.client.ClientChatChannel;
import colab.common.channel.ChatChannelData;
import colab.common.naming.UserName;

public class ChatChannelFrame extends JFrame {
    private final ClientChatChannel channel;
    private final ChatPanel chatPanel;
    public ChatChannelFrame(ClientChatChannel clientChannel, final UserName name) {
        channel = clientChannel;
        chatPanel = new ChatPanel(name);

        chatPanel.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               String mess;
               while((mess = chatPanel.dequeuePendingMessage()) != null) {
                   try
                   {
                       channel.add(new ChatChannelData(mess, name));
                   }
                   catch(RemoteException ex)
                   {
//                       REALLY CUTESY FLAG FOR CHRIS!!!!!!!!!!!!!!!!!!
//                       ~ <(^.^)> ~
                   }
               }

           }
        });

        setTitle(channel.getId().toString());
        setSize(300, 300);
        setResizable(false);

        setLayout(new FlowLayout());
        add(chatPanel);
    }
}
