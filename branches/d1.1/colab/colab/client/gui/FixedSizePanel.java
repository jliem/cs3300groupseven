package colab.client.gui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * Wraps a fixed-size panel so it can be placed in the middle
 * of a container without being resized.
 */
class FixedSizePanel extends JPanel {

    /** Serialization version number. */
    private static final long serialVersionUID = 1L;

    /** The fixed size of the interior panel. */
    private Dimension size;

    /** The panel being wrapped. */
    private JPanel innerPanel;

    public FixedSizePanel(final JPanel jpanel, final Dimension size) {
        this(jpanel);
        setFixedSize(size);
    }

    public FixedSizePanel(final JPanel jpanel) {
        innerPanel = new JPanel();
        innerPanel = jpanel;
        size = innerPanel.getPreferredSize();
        setOpaque(false);
        setLayout(new BoxLayout(this, 1));
        JPanel jpanel1 = new JPanel();
        jpanel1.setOpaque(false);
        jpanel1.setLayout(new BoxLayout(jpanel1, 1));
        jpanel1.add(Box.createVerticalGlue());
        jpanel1.add(innerPanel);
        jpanel1.add(Box.createVerticalGlue());
        add(Box.createHorizontalGlue());
        add(jpanel1);
        add(Box.createHorizontalGlue());
        setSize(size);
    }

    public void setFixedSize(final Dimension dimension) {
        innerPanel.setMinimumSize(dimension);
        innerPanel.setPreferredSize(dimension);
        innerPanel.setMaximumSize(dimension);
    }

}
