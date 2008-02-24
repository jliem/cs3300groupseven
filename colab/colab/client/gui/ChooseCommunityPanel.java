package colab.client.gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import colab.client.ColabClient;

public class ChooseCommunityPanel extends JPanel {

	private JLabel selectLabel;
	private JButton selectButton;
	private JComboBox selectBox;
	
	private final ArrayList<ActionListener> listeners;
	
	// private

	public ChooseCommunityPanel() {
	    
	    listeners = new ArrayList<ActionListener>();
	    
	    selectLabel = new JLabel(
				"Select the community you wish to visit for this session: ");

		selectButton = new JButton("Select");
		
		selectBox = new JComboBox();

		selectButton.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e){
		        fireActionPerformed(e);
		    }
		});

		setLayout(new GridLayout(3, 1));

		add(selectLabel);
		add(selectBox);
		add(selectButton);

	}
	
	public void setCommunityNames(Object[] names){
	    
	    for(Object name: names)
	        selectBox.addItem(name.toString());
	    
	}
	
	public void addActionListener(ActionListener l) {
	    listeners.add(l);
	}
	
	public void fireActionPerformed(ActionEvent e)
	{
	    for(ActionListener l : listeners) {
	        l.actionPerformed(e);
	    }
	}
	
	public String getCurrentCommunityName(){
	    return selectBox.getSelectedItem().toString();
	}
	

}
