package colab.client.gui.whiteboard;

import java.awt.Dimension;

import colab.client.ClientWhiteboardChannel;
import colab.client.ColabClient;
import colab.client.gui.ClientChannelFrame;
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

        this.setPreferredSize(new Dimension(800, 600));

    }

}
