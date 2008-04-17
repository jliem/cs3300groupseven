package colab.client.gui.whiteboard;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import colab.client.ClientWhiteboardChannel;
import colab.client.gui.ClientChannelPanel;
import colab.common.naming.UserName;

public class WhiteboardChannelPanel extends ClientChannelPanel {

    public static final long serialVersionUID = 1;

    private final ClientWhiteboardChannel channel;
    
    private WhiteboardChannelToolPanel toolPanel;
    private DrawingPanel drawingPanel;
    private JColorChooser colorChooser;
    
    protected enum ToolType {
        PATH,
        ELLIPSE,
        RECTANGLE
    }

    public WhiteboardChannelPanel(final UserName name, ClientWhiteboardChannel channel) {
        super(name);

        this.channel = channel;
        drawingPanel = new DrawingPanel();
        drawingPanel.setPreferredSize(new Dimension(500, 500));
        toolPanel = new WhiteboardChannelToolPanel();
        colorChooser = new JColorChooser();
        colorChooser.setPreferredSize(new Dimension(150, 275));
        
        this.setLayout(new BorderLayout());
        add(toolPanel, BorderLayout.WEST);
        add(drawingPanel, BorderLayout.EAST);
        add(colorChooser, BorderLayout.SOUTH);
        this.setVisible(true);
       
    }
    
    public static void main(String[] args){
    	JFrame frame = new JFrame("test");
    	frame.add(new WhiteboardChannelPanel(null, null));
    	frame.pack();
    	frame.setVisible(true);
    	
    }
}
