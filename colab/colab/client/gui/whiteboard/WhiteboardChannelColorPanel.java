package colab.client.gui.whiteboard;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.JPanel;

public class WhiteboardChannelColorPanel extends JPanel {

	JTextField[] colorTextFields;
	private final List<ActionListener> listeners;
	
	public WhiteboardChannelColorPanel() {
		colorTextFields = new JTextField[16];
		listeners = new ArrayList<ActionListener>();
		
		for (int i = 0; i < 16; i++) {
			colorTextFields[i] = new JTextField();
//			colorTextFields[i].addActionListener(new ActionListener() {
//				
//				public void actionPerformed(final ActionEvent e) {
//					fireActionPerformed(new ActionEvent(this,
//							ActionEvent.ACTION_FIRST, "Color chosen", i));
//				}
//			});
		}

		this.setLayout(new GridLayout(4, 4));

		for (int i = 15; i >= 0; i--) {
			this.add(colorTextFields[i]);
		}

	}

	public void fireActionPerformed(final ActionEvent e, int index) {
		for (ActionListener l : listeners) {
			l.actionPerformed(e);
		}
		
		setCurrentColor(index);
	}
	
	public void addActionListener(final ActionListener l) {
        listeners.add(l);
    }

	public void setCurrentColor(int i){
		
	}
}
