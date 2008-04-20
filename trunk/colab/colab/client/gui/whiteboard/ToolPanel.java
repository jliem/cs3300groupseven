package colab.client.gui.whiteboard;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import colab.client.gui.whiteboard.draw.EllipseDrawingTool;
import colab.client.gui.whiteboard.draw.LineDrawingTool;
import colab.client.gui.whiteboard.draw.PathDrawingTool;
import colab.client.gui.whiteboard.draw.RectangleDrawingTool;

public class ToolPanel extends JPanel{

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private JButton pencilButton;
    private JButton solidEllipseButton;
    private JButton clearEllipseButton;
    private JButton solidRectangleButton;
    private JButton clearRectangleButton;
    private JButton lineButton;
    //private JColorChooser colorChooser;

    public ToolPanel(
            final WhiteboardChannelPanel parentPanel) {

        pencilButton = new JButton(new ImageIcon("pencil.gif"));
        solidEllipseButton = new JButton(new ImageIcon("solidellipse.gif"));
        lineButton = new JButton(new ImageIcon("line.gif"));
        clearEllipseButton = new JButton(new ImageIcon("clearellipse.gif"));
        solidRectangleButton = new JButton(new ImageIcon("solidrect.gif"));
        clearRectangleButton = new JButton(new ImageIcon("clearrectangle.gif"));

        pencilButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                parentPanel.setTool(new PathDrawingTool(parentPanel));
            }
        });

        lineButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                parentPanel.setTool(new LineDrawingTool(parentPanel));
            }
        });

        solidRectangleButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                parentPanel.setTool(
                        new RectangleDrawingTool(parentPanel, true));
            }
        });

        clearRectangleButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                parentPanel.setTool(
                        new RectangleDrawingTool(parentPanel, false));
            }
        });

        solidEllipseButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                parentPanel.setTool(
                        new EllipseDrawingTool(parentPanel, true));
            }
        });

        clearEllipseButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                parentPanel.setTool(
                        new EllipseDrawingTool(parentPanel, false));
            }
        });

        setLayout(new GridLayout(3, 1));

        add(pencilButton);
        add(lineButton);
        add(solidEllipseButton);
        add(clearEllipseButton);
        add(solidRectangleButton);
        add(clearRectangleButton);

        setVisible(true);

    }

}
