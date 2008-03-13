package colab.client.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import colab.client.ClientChatChannel;
import colab.client.ColabClient;
import colab.common.exception.NetworkException;
import colab.common.exception.UserAlreadyExistsException;
import colab.common.naming.CommunityName;
import colab.server.user.Password;

public class NewCommunityFrame extends JFrame {

	private final JButton createButton;
	private final JTextField commName;
	private final JPasswordField commPass, confirmCommPass;
	private final JLabel nameLabel, passLabel, confirmPassLabel;
	private final ColabClient client;

	public NewCommunityFrame(final ColabClient client) {

		this.client = client;
		createButton = new JButton("Create Community");
		commName = new JTextField("");
		commPass = new JPasswordField("");
		confirmCommPass = new JPasswordField("");
		nameLabel = new JLabel("Community name: ");
		passLabel = new JLabel("Community password: ");
		confirmPassLabel = new JLabel("Confirm community password: ");

		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (commName.getText() != null
						&& commPass.getPassword() != null
						&& confirmCommPass.getPassword() != null) {

					if (commPass.getPassword().toString().compareTo(
							confirmCommPass.getPassword().toString()) != 0) {
						showErrorBox(
								"The password fields do not agree.  Please enter the desired password again."
										+ "(The password is case sensitive!)",
								"Password Error");
						commPass.setText("");
						confirmCommPass.setText("");
					}

					if (commPass.getPassword().toString().compareTo(
							confirmCommPass.getPassword().toString()) == 0) {
						boolean flagged = false;
						try {
							for (CommunityName name : client
									.getAllCommunityNames()) {
								if (name.toString().equalsIgnoreCase(
										commName.getText())) {
									showErrorBox(
											"A community with that name already exits; please enter a new community "
													+ "name.",
											"Community Name Error");
									commName.setText("");
									flagged = true;
								}
							}
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						if (!flagged) {
							try {
								client.createCommunity(new CommunityName(
										commName.getText()), new Password(
										commPass.getPassword().toString()));
							} catch (NetworkException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (UserAlreadyExistsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}

				}
			}
		});
		
		
		//this.setPreferredSize(new Dimension(800,400));
		setLayout(new GridLayout(4,2));
		add(nameLabel);
		add(commName);
		add(passLabel);
		add(commPass);
		add(confirmPassLabel);
		add(confirmCommPass);
		add(createButton);
		
	}

	private void showErrorBox(String message, String title) {
		JOptionPane.showMessageDialog(this, message, title,
				JOptionPane.ERROR_MESSAGE);
	}

}