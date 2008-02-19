package colab.client;
import java.awt.*;
import javax.swing.*;

public class LoginPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel usernameLabel, passwordLabel, serverLabel;
	private JPasswordField password;
	private JTextField username, serverIP;
	private JButton loginButton;
	//private 
	
	public LoginPanel(){
		
		usernameLabel = new JLabel("Username: ");
		passwordLabel = new JLabel("Password: ");
		serverLabel = new JLabel("Choose server: ");
		username = new JTextField(12);
		password = new JPasswordField(12);
		serverIP = new JTextField(32);
		loginButton = new JButton("Login");
		
		loginButton.addActionListener(new ColabLogin(username.getText(), password.getPassword(), serverIP.getText()));
		
		setLayout(new GridLayout(2, 3));
		
		add(usernameLabel);
		add(username);
		add(passwordLabel);
		add(password);
		add(serverLabel);
		add(serverIP);
		add(loginButton);
		
		
		
		
		
		
	}
	
	public static void main(String[] args){
		Frame f = new Frame("CoLab Login");
		LoginPanel loginPanel = new LoginPanel();

		f.add(loginPanel);

		f.setSize(400, 100);
		f.setVisible(true);
		
	}
	
	
	

}
