package colab.client.gui;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends JPanel {

    /** Serialization version number. */
    private static final long serialVersionUID = 1L;

    private JLabel usernameLabel, passwordLabel, serverLabel;
    private JPasswordField password;
    private JTextField username, serverIP;
    private JButton loginButton;
    //private

    public LoginPanel(){

        usernameLabel = new JLabel("Username / Desired Username: ");
        passwordLabel = new JLabel("Password / Desired Password: ");
        serverLabel = new JLabel("Enter server IP: ");
        username = new JTextField(12);
        password = new JPasswordField(12);
        serverIP = new JTextField(32);
        loginButton = new JButton("Submit");
        

        ColabLogin login = new ColabLogin(
                username.getText(), password.getPassword(), serverIP.getText());
        loginButton.addActionListener(login);

        setLayout(new GridLayout(4, 2));

        add(serverLabel);
        add(serverIP);
        add(usernameLabel);
        add(username);
        add(passwordLabel);
        add(password);
        add(loginButton);

    }

    public static void main(final String[] args) {
        JFrame f = new JFrame("CoLab Login");
        LoginPanel loginPanel = new LoginPanel();

        f.add(loginPanel);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 100);
        f.setVisible(true);

    }

}
