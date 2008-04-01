package colab.client.gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;

import colab.client.ClientDocumentChannel;
import colab.client.ColabClient;
import colab.common.channel.ChannelData;
import colab.common.naming.ChannelName;
import colab.common.naming.UserName;

/**
 * Frame for a document channel.
 */
public class DocumentChannelFrame extends ClientChannelFrame {

    /**
     * Serial version UID.
     */
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
                new DocumentPanel(name, clientChannel.getCurrentDocument()));

        channel = clientChannel;

        // Cast the parent's generic version to a ChatPanel for convenience
        documentPanel = (DocumentPanel) getClientChannelPanel();

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
