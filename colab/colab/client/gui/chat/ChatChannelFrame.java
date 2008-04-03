package colab.client.gui.chat;

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
import colab.client.gui.ClientChannelFrame;
import colab.client.gui.revision.RevisionChatPanel;
import colab.client.gui.revision.RevisionFrame;
import colab.common.channel.ChannelData;
import colab.common.channel.ChatChannelData;
import colab.common.exception.ConnectionDroppedException;
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

        super(client, clientChannel, new ChatPanel(name));

        channel = clientChannel;

        chatPanel = (ChatPanel) getClientChannelPanel();

        try {
            List<ChannelData> data = client.getLastData(channel.getId(), -1);
            for (final ChannelData d : data) {
                chatPanel.writeMessage((ChatChannelData) d);
            }
        } catch (final ConnectionDroppedException cde) {
            System.exit(1);
        }

        chatPanel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                ChatChannelData mess;
                while ((mess = chatPanel.dequeuePendingMessage()) != null) {
                    clientChannel.addLocal(mess);
                    try {
                        client.add(channel.getId(), mess);
                    } catch (ConnectionDroppedException cde) {
                        System.exit(1);
                    }
                }
            }
        });

        channel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
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

        JMenuItem revisionMode = new JMenuItem("Revision mode");
        revisionMode.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent arg0) {
                RevisionFrame frame = new RevisionFrame(
                        new RevisionChatPanel(channel,
                                channel.getChannelData()));

                frame.pack();
                frame.setVisible(true);
            }

        });

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

        fileMenu.add(revisionMode);
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

            /** Serialization version number. */
            public static final long serialVersionUID = 1L;

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
