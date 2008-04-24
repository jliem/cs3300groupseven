package colab.client.gui.whiteboard;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import colab.client.ClientWhiteboardChannel;
import colab.client.ColabClient;
import colab.client.gui.ChannelPanelListener;
import colab.client.gui.ClientChannelFrame;
import colab.client.gui.ExportFrame;
import colab.client.gui.revision.RevisionFrame;
import colab.client.gui.revision.RevisionWhiteboardPanel;
import colab.common.DebugManager;
import colab.common.channel.ChannelData;
import colab.common.exception.ConnectionDroppedException;
import colab.common.naming.UserName;

public class WhiteboardChannelFrame extends ClientChannelFrame {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private ClientWhiteboardChannel channel;

    private WhiteboardChannelPanel whiteboardPanel;

    public  WhiteboardChannelFrame(final ColabClient client,
            final ClientWhiteboardChannel clientChannel,
            final UserName name) {

        super(client,
            clientChannel,
            new WhiteboardChannelPanel(name, clientChannel));

        channel = clientChannel;

        // Cast the parent's generic version to a
        // WhiteboardPanel for convenience
        whiteboardPanel = (WhiteboardChannelPanel) getClientChannelPanel();

        whiteboardPanel.addChannelPanelListener(new ChannelPanelListener() {

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
                //if (!(d instanceof EditDocChannelData)) {
                channel.add(d);
            }
        } catch (final ConnectionDroppedException cde) {
            DebugManager.connectionDropped(cde);
            System.exit(1);
        } catch (RemoteException e) {
            DebugManager.remote(e);
            System.exit(1);
        }

        whiteboardPanel.refreshLayerList();

//        if (whiteboardPanel.getNumberOfLayers() == 0) {
//            whiteboardPanel.createNewLayer(null);
//        }

        // Menu
        JMenuBar menu = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem revisionMode = new JMenuItem("Revision mode");
        revisionMode.addActionListener(new ActionListener() {
            @SuppressWarnings("unchecked")
            public void actionPerformed(final ActionEvent arg0) {
                RevisionFrame frame = new RevisionFrame(
                        new RevisionWhiteboardPanel(channel,
                                channel.getChannelData()));

                frame.pack();
                frame.setVisible(true);
            }

        });

        JMenuItem export = new JMenuItem("Export Whiteboard");
        export.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
                ActionEvent.ALT_MASK));
        export.getAccessibleContext().setAccessibleDescription(
                "Exports the whiteboard to a file.");
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

}
