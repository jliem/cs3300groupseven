package colab.client.gui.whiteboard;

import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class WhiteboardChannelToolPanel extends JPanel{

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private JButton pencilButton;
    private JButton solidEllipseButton;
    private JButton clearEllipseButton;
    private JButton solidRectangleButton;
    private JButton clearRectangleButton;
    private JButton eraserButton;
    //private JColorChooser colorChooser;

    public WhiteboardChannelToolPanel(){

        pencilButton = new JButton(new ImageIcon("pencil.gif"));
        solidEllipseButton = new JButton(new ImageIcon("solidellipse.gif"));
        eraserButton = new JButton(new ImageIcon("eraser.gif"));
        clearEllipseButton = new JButton(new ImageIcon("clearellipse.gif"));
        solidRectangleButton = new JButton(new ImageIcon("solidrect.gif"));
        clearRectangleButton = new JButton(new ImageIcon("clearrectangle.gif"));
        //colorChooser = new JColorChooser();

        //colorChooser.setPreferredSize(new Dimension(10,10));
        this.setLayout(new GridLayout(3, 1));

        add(pencilButton);
        add(eraserButton);
        add(solidEllipseButton);
        add(clearEllipseButton);
        add(solidRectangleButton);
        add(clearRectangleButton);
        //add(colorChooser);

        this.setVisible(true);

    }

}
