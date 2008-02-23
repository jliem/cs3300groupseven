package colab.client.gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChooseCommunityPanel extends JPanel {

	private JLabel selectLabel;
	private JButton selectButton;
	private JComboBox selectBox;

	// private

	public ChooseCommunityPanel() {

		selectLabel = new JLabel(
				"Select the community you wish to visit for this session: ");

		selectButton = new JButton("Select");
		
		selectBox = new JComboBox();

		// selectButton.addActionListener(login);

		setLayout(new GridLayout(3, 1));

		add(selectLabel);
		add(selectBox);
		add(selectButton);

	}

}
