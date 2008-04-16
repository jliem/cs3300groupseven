package colab.client.gui.whiteboard;

import colab.client.ClientWhiteboardChannel;
import colab.client.ColabClient;
import colab.client.gui.ClientChannelFrame;
import colab.common.naming.UserName;

public class WhiteboardChannelFrame extends ClientChannelFrame {
        public  WhiteboardChannelFrame(final ColabClient client,
                final ClientWhiteboardChannel clientChannel, final UserName name) {
            super(client, clientChannel, new WhiteboardChannelPanel(name, clientChannel));
        }
}
