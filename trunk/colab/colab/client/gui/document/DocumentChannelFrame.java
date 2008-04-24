package colab.client.gui.document;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import colab.client.ClientDocumentChannel;
import colab.client.ColabClient;
import colab.client.gui.ChannelPanelListener;
import colab.client.gui.ClientChannelFrame;
import colab.client.gui.ExportFrame;
import colab.client.gui.revision.RevisionDocumentPanel;
import colab.client.gui.revision.RevisionFrame;
import colab.common.DebugManager;
import colab.common.channel.ChannelData;
import colab.common.channel.document.EditDocChannelData;
import colab.common.exception.ConnectionDroppedException;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;

/**
 * Frame for a document channel.
 */
public class DocumentChannelFrame extends ClientChannelFrame {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /** The channel. */
    private final ClientDocumentChannel channel;

    /** The document panel. */
    private final DocumentPanel documentPanel;

    /**
     * Creates a new DocumentChannelFrame.
     *
     * @param client the colab client
     * @param clientChannel the document channel
     * @param name the name of the logged in user
     */
    public DocumentChannelFrame(final ColabClient client,
            final ClientDocumentChannel clientChannel, final UserName name) {

        super(client, clientChannel,
                new DocumentPanel(name, clientChannel));

        channel = clientChannel;

        // Cast the parent's generic version to a DocumentPanel for convenience
        documentPanel = (DocumentPanel) getClientChannelPanel();

        // Add a listener so this frame can repaint itself when the panels
        // have changed--necessary to fix ugly GUI transitions
        documentPanel.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent arg0) {
                if (arg0.getActionCommand().equals("panels arranged")) {
                    pack();
                    repaint();
                }
            }
        });

        documentPanel.addChannelPanelListener(new ChannelPanelListener() {

            public void onMessageSent(final ChannelData data) {

                try {
                    client.add(channel.getId(), data);
                } catch (ConnectionDroppedException cde) {
                    DebugManager.connectionDropped(cde);
                    System.exit(1);
                }

            }

        });

        // Download existing data from server
        try {
            List<ChannelData> data = client.getLastData(channel.getId(), -1);
            for (final ChannelData d : data) {
                if (!(d instanceof EditDocChannelData)) {
                    channel.add(d);
                }
            }
        } catch (final ConnectionDroppedException cde) {
            DebugManager.connectionDropped(cde);
            System.exit(1);
        } catch (RemoteException e) {
            DebugManager.remote(e);
            System.exit(1);
        }

        documentPanel.refreshDocument();

        // Only create new paragraph if document is blank otherwise
        if (documentPanel.getNumberOfEditors() <= 0) {
            documentPanel.createNewParagraph(null);
        }


        // Menu
        JMenuBar menu = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem revisionMode = new JMenuItem("Revision mode");
        revisionMode.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent arg0) {
                RevisionFrame frame = new RevisionFrame(
                        new RevisionDocumentPanel(channel,
                                channel.getChannelData()));

                frame.pack();
                frame.setVisible(true);
            }

        });

        JMenuItem export = new JMenuItem("Export Document");
        export.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
                ActionEvent.ALT_MASK));
        export.getAccessibleContext().setAccessibleDescription(
                "Exports the document to a file.");
        JMenuItem exit = new JMenuItem("Exit");
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                ActionEvent.ALT_MASK));
        exit.getAccessibleContext().setAccessibleDescription(
                "Leaves the channel.");

        export.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                ExportFrame frame = new ExportFrame(channel);
                frame.pack();
                frame.setVisible(true);
            }
        });

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                exit();
            }
        });

        fileMenu.add(revisionMode);
        fileMenu.add(export);
        fileMenu.add(exit);

        menu.add(fileMenu);

        this.setJMenuBar(menu);

        this.setPreferredSize(new Dimension(800, 600));

    }

    public static void main(final String[] args) throws Exception {

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
        ClientDocumentChannel channel = new ClientDocumentChannel(channelName);
        UserName username = new UserName("test");

        DocumentChannelFrame docFrame =
            new DocumentChannelFrame(client, channel, username);
        docFrame.pack();
        docFrame.setVisible(true);

    }

}
