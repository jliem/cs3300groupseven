package colab.client.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JFrame;

import colab.client.ClientChatChannel;
import colab.client.ColabClient;
import colab.common.channel.ChatChannelData;
import colab.common.naming.UserName;

public class ChatChannelFrame extends JFrame {
    private final ColabClient client;
    private final ClientChatChannel channel;
    private final ChatPanel chatPanel;
    public ChatChannelFrame(final ColabClient client,
            ClientChatChannel clientChannel, final UserName name) {
        this.client = client;
        channel = clientChannel;
        chatPanel = new ChatPanel(name);

        chatPanel.addActionListener(new ActionListener() {
           public void actionPerformed(final ActionEvent e) {
               String mess;
               while((mess = chatPanel.dequeuePendingMessage()) != null) {
                   try
                   {
                       client.add(channel.getId(), new ChatChannelData(mess, name));
                   }
                   catch(RemoteException ex)
                   {
//                       REALLY CUTESY FLAG FOR CHRIS!!!!!!!!!!!!!!!!!!
//                       ~ <(^.^)> ~
                   }
               }

           }
        });

        channel.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               for(String mess : channel.getNewMessages()) {
                   chatPanel.writeMessage(mess);
               }
           }
        });

        setTitle(channel.getId().toString());
        setSize(320, 300);

        setLayout(new FlowLayout());
        add(chatPanel);
    }
}
