package colab.client.gui.whiteboard;

import java.awt.Dimension;
import java.rmi.RemoteException;
import java.util.List;

import colab.client.ClientWhiteboardChannel;
import colab.client.ColabClient;
import colab.client.gui.ChannelPanelListener;
import colab.client.gui.ClientChannelFrame;
import colab.common.DebugManager;
import colab.common.channel.ChannelData;
import colab.common.channel.document.EditDocChannelData;
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

                DebugManager.debug("CALL TO SERVER ...");

                try {
                    client.add(channel.getId(), data);
                } catch (ConnectionDroppedException cde) {

                    DebugManager.debug("... FAILED");

                    DebugManager.connectionDropped(cde);
                    System.exit(1);
                }

                DebugManager.debug("... RESPONDED");

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

        if (whiteboardPanel.getNumberOfLayers() <= 0) {
            whiteboardPanel.createNewLayer(null);
        }


        this.setPreferredSize(new Dimension(800, 600));

    }

}
