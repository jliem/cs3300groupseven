package colab.client.gui;

import java.awt.Dimension;

import colab.client.ClientDocumentChannel;
import colab.client.ColabClient;
import colab.common.naming.UserName;

/**
 * Frame for a document channel.
 *
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

        // TODO: This is ugly, think of a better way
        // that doesn't involve passing a new panel to the parent
        super(client, clientChannel, new DocumentPanel(name));

        channel = clientChannel;

        // TODO: This is ugly, think of a better way
        // that doesn't involve retrieving the panel from the parent
        // Cast the parent's generic version to a ChatPanel for convenience
        documentPanel = (DocumentPanel) getClientChannelPanel();

        this.setPreferredSize(new Dimension(800, 600));

    }


}
