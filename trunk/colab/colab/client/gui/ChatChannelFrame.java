package colab.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import colab.client.ClientChatChannel;
import colab.client.ColabClient;
import colab.common.DebugManager;
import colab.common.channel.ChannelData;
import colab.common.channel.ChatChannelData;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;

public final class ChatChannelFrame extends ClientChannelFrame {

    private final ClientChatChannel channel;

    private final ChatPanel chatPanel;

    private final JMenuBar menu;

    private final JMenu fileMenu, optionsMenu;

    private final JMenuItem export, exit;

    private final JCheckBoxMenuItem timestampCheckBox;

    private static final long serialVersionUID = 1;

    public ChatChannelFrame(final ColabClient client,
            final ClientChatChannel clientChannel, final UserName name) {

        // TODO: This is ugly, think of a better way
        // that doesn't involve passing a new panel to the parent
        super(client, clientChannel, new ChatPanel(name));

        channel = clientChannel;

        // TODO: This is ugly, think of a better way
        // that doesn't involve retrieving the panel from the parent
        // Cast the parent's generic version to a ChatPanel for convenience
        chatPanel = (ChatPanel)(super.clientChannelPanel);

        try {
            List<ChannelData> data = client.getLastData(channel.getId(), -1);
            for (final ChannelData d : data) {
                chatPanel.writeMessage((ChatChannelData) d);
            }
        } catch (RemoteException ex) {
            // TODO: handler remote chat exceptions
            // REALLY CUTESY FLAG FOR CHRIS!!!!!!!!!!!!!!!!!!
            // ~ <(^.^)> ~
            DebugManager.remote(ex);
        }

        chatPanel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                ChatChannelData mess;
                while ((mess = chatPanel.dequeuePendingMessage()) != null) {
                    try {
                        clientChannel.add(mess);
                        client.add(channel.getId(), mess);
                    } catch (RemoteException ex) {
                        // TODO: handler remote chat exceptions
                        // REALLY CUTESY FLAG FOR CHRIS!!!!!!!!!!!!!!!!!!
                        // ~ <(^.^)> ~
                        DebugManager.remote(ex);
                    }
                }

            }
        });

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
        timestampCheckBox.getAccessibleContext().setAccessibleDescription(
                "Enables/disables chat timestamps.");

        export.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                ExportChatFrame frame = new ExportChatFrame(channel);
                frame.pack();
                frame.setVisible(true);
            }
        });

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                exit();
            }
        });

        timestampCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(final ChangeEvent e) {
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

        add(chatPanel);
    }

    public static void main(final String[] args) throws RemoteException {

        ColabClient client = new ColabClient() {
            public Collection<UserName> getActiveUsers(final ChannelName name) {
                return new ArrayList<UserName>();
            }
            public List<ChannelData> getLastData(
                    final ChannelName a, final int c) {
                return new ArrayList<ChannelData>();
            }
        };

        ChannelName channelName = new ChannelName("Test Channel");
        ClientChatChannel channel = new ClientChatChannel(channelName);
        UserName username = new UserName("test");

        ChatChannelFrame chat = new ChatChannelFrame(client, channel, username);
        chat.pack();
        chat.setVisible(true);

    }

    /**
     * Getter for timestamp state.
     * @return flag for enabled timestamp
     */
    public boolean isTimestampEnabled() {
        return chatPanel.isTimestampEnabled();
    }

    /**
     * Toggles timestamp state.
     * @param timestampEnabled true if timestamp is enabled
     */
    public void setTimestampEnabled(final boolean timestampEnabled) {
        chatPanel.setTimestampEnabled(timestampEnabled);
    }

}
