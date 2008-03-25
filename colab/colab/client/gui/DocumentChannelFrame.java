package colab.client.gui;

import colab.client.ClientChatChannel;
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
    private static final long serialVersionUID = 1L;

    public DocumentChannelFrame(final ColabClient client,
            final ClientChatChannel clientChannel, final UserName name) {

        super(client, clientChannel, new DocumentPanel(name));

    }


}
