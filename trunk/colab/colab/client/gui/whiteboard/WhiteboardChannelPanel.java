package colab.client.gui.whiteboard;

import colab.client.ClientWhiteboardChannel;
import colab.client.gui.ClientChannelPanel;
import colab.common.naming.UserName;

public class WhiteboardChannelPanel extends ClientChannelPanel {
    
    public static final long serialVersionUID = 1;
    
    private final ClientWhiteboardChannel channel;
    
    public WhiteboardChannelPanel(final UserName name, ClientWhiteboardChannel channel) {
        super(name);
        
        this.channel = channel;
    }
}
