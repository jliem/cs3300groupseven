package colab.client.gui.whiteboard;

import java.awt.BorderLayout;

import javax.swing.JButton;

import colab.client.ClientWhiteboardChannel;
import colab.client.gui.ClientChannelPanel;
import colab.common.naming.UserName;

public class WhiteboardChannelPanel extends ClientChannelPanel {

    public static final long serialVersionUID = 1;

    private final ClientWhiteboardChannel channel;


    protected enum ToolType {
        PATH,
        ELLIPSE,
        RECTANGLE
    }

    public WhiteboardChannelPanel(final UserName name, ClientWhiteboardChannel channel) {
        super(name);

        this.channel = channel;

        this.setLayout(new BorderLayout());

        this.add(new DrawingPanel(), BorderLayout.CENTER);
    }
}
