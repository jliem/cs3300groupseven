package colab.client.gui.whiteboard;

import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class WhiteboardChannelToolPanel extends JPanel{
	
	private JButton pencilButton;
	private JButton solidEllipseButton;
	private JButton clearEllipseButton;
	private JButton solidRectangleButton;
	private JButton clearRectangleButton;
	private JButton eraserButton;
	private WhiteboardChannelColorPanel colorPanel;
	
	public WhiteboardChannelToolPanel(){
		colorPanel = new WhiteboardChannelColorPanel();
		
		pencilButton = new JButton(new ImageIcon("pencil.gif"));
		//pencilButton.setSize(new Dimension( (new ImageIcon("sa.jpeg")).getIconWidth(), (new ImageIcon("sa.jpeg")).getIconHeight()) );
		solidEllipseButton = new JButton(new ImageIcon("solidellipse.gif"));
		eraserButton = new JButton(new ImageIcon("eraser.gif"));
		clearEllipseButton = new JButton(new ImageIcon("clearellipse.gif"));
		solidRectangleButton = new JButton(new ImageIcon("solidrect.gif"));
		clearRectangleButton = new JButton (new ImageIcon("clearrectangle.gif"));
		
		this.setLayout(new GridLayout(5,1));
		
		add(pencilButton);
		add(eraserButton);
		add(solidEllipseButton);
		add(clearEllipseButton);
		add(solidRectangleButton);
		add(clearRectangleButton);
		add(colorPanel);
		
		this.setVisible(true);
		
	}
	
		
	

}
