package colab.client.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import colab.client.ClientChatChannel;
import colab.client.ColabClient;
import colab.common.channel.ChannelData;
import colab.common.channel.ChatChannelData;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;

public class ChatChannelFrame extends JFrame {
    private final ColabClient client;

    private final ClientChatChannel channel;

    private final ChatPanel chatPanel;

    private final JMenuBar menu;

    private final JMenu fileMenu, optionsMenu;

    private final JMenuItem export, exit;

    private final JCheckBoxMenuItem timestampCheckBox;

    private static final long serialVersionUID = 1;

    public ChatChannelFrame(final ColabClient client,
            final ClientChatChannel clientChannel, final UserName name) {
        this.client = client;
        channel = clientChannel;
        chatPanel = new ChatPanel(name);

        try {
            List<ChannelData> data = client.getLastData(channel.getId(), -1);
            for (final ChannelData d : data) {
                chatPanel.writeMessage((ChatChannelData) d);
            }
        }
        catch (RemoteException ex) {
            //TODO: handler remote chat exceptions
            // REALLY CUTESY FLAG FOR CHRIS!!!!!!!!!!!!!!!!!!
            // ~ <(^.^)> ~
        }

        chatPanel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                ChatChannelData mess;
                while ((mess = chatPanel.dequeuePendingMessage()) != null) {
                    try {
                        clientChannel.add(mess);
                        client.add(channel.getId(), mess);
                    } catch (RemoteException ex) {
                        //TODO: handler remote chat exceptions
                        // REALLY CUTESY FLAG FOR CHRIS!!!!!!!!!!!!!!!!!!
                        // ~ <(^.^)> ~
                    }
                }

            }
        });


        channel.addUserListener(chatPanel.getUserListPanel());

        channel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (ChatChannelData mess : channel.getNewMessages()) {
                    chatPanel.writeMessage(mess);
                }
            }
        });

        menu = new JMenuBar();
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        optionsMenu = new JMenu("Options");
        optionsMenu.setMnemonic(KeyEvent.VK_O);


        export = new JMenuItem("Export Chat");
        export.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
                ActionEvent.ALT_MASK));
        export.getAccessibleContext().setAccessibleDescription(
                "Exports the conversation to a text file.");
        exit = new JMenuItem("Exit");
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                ActionEvent.ALT_MASK));
        exit.getAccessibleContext().setAccessibleDescription(
                "Leaves the channel.");
        timestampCheckBox = new JCheckBoxMenuItem("Timestamp");
        timestampCheckBox.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
                ActionEvent.ALT_MASK));
        timestampCheckBox.getAccessibleContext().setAccessibleDescription("Enables/disables chat timestamps.");

        export.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ExportChatFrame frame = new ExportChatFrame(channel);
                frame.setVisible(true);
            }
        });

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        timestampCheckBox.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e){
                setTimestampEnabled(timestampCheckBox.getState());
            }
        });
        timestampCheckBox.setSelected(false);

        fileMenu.add(export);
        fileMenu.add(exit);

        optionsMenu.add(timestampCheckBox);

        menu.add(fileMenu);
        menu.add(optionsMenu);

        this.setJMenuBar(menu);

        setTitle(channel.getId().toString());
        setSize(new Dimension(320, 300));

        // and since we'd like the channel to be exited on close...
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        add(chatPanel);
    }

    protected void exit() {
        // TODO:add "leave channel" code
        setVisible(false);
        dispose();
    }

    public static void main(String args[]) throws RemoteException {
        ChatChannelFrame chat = new ChatChannelFrame(new ColabClient(),
                new ClientChatChannel(new ChannelName("Test Channel")),
                new UserName("test"));
        chat.setVisible(true);
    }

    public boolean isTimestampEnabled() {
        return chatPanel.isTimestampEnabled();
    }

    public void setTimestampEnabled(boolean timestampEnabled) {
        chatPanel.setTimestampEnabled(timestampEnabled);
    }
}
