package colab.client.gui.whiteboard;

import javax.swing.JFrame;
import java.awt.BorderLayout;

import javax.swing.JButton;
import colab.client.ClientWhiteboardChannel;
import colab.client.gui.ClientChannelPanel;
import colab.common.naming.UserName;

public class WhiteboardChannelPanel extends ClientChannelPanel {

    public static final long serialVersionUID = 1;

    private final ClientWhiteboardChannel channel;
    
    private WhiteboardChannelToolPanel toolPanel;
    
    protected enum ToolType {
        PATH,
        ELLIPSE,
        RECTANGLE
    }

    public WhiteboardChannelPanel(final UserName name, ClientWhiteboardChannel channel) {
        super(name);

        this.channel = channel;
        toolPanel = new WhiteboardChannelToolPanel();
        add(toolPanel);
        
        
        this.setVisible(true);
    }
    
    public static void main(String[] args){
    	JFrame frame = new JFrame("test");
    	frame.add(new WhiteboardChannelPanel(null, null));
    	frame.pack();
    	frame.setVisible(true);
    	
    }
}
