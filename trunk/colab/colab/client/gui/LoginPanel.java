package colab.client.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import colab.client.ColabClient;
import colab.common.exception.UnableToConnectException;
import colab.common.remote.exception.AuthenticationException;

public class LoginPanel extends JPanel {

    /** Serialization version number. */
    private static final long serialVersionUID = 1L;

    private JLabel usernameLabel, passwordLabel, serverLabel;
    private JPasswordField password;
    private JTextField username, serverIP;
    private JButton loginButton;
    private String currentUser;
    private ArrayList<ActionListener> listeners;

    public LoginPanel(final ColabClient client) {


        usernameLabel = new JLabel("Username / Desired Username: ");
        passwordLabel = new JLabel("Password / Desired Password: ");
        serverLabel = new JLabel("Enter server IP: ");
        username = new JTextField(12);
        password = new JPasswordField(12);
        serverIP = new JTextField(32);
        loginButton = new JButton("Submit");

        listeners = new ArrayList<ActionListener>();

        final Runnable loginTask = new Runnable() {
            public void run() {
                try {

                    currentUser = username.getText();
                    client.loginUser(username.getText(),
                            password.getPassword(), serverIP.getText());

                    fireActionPerformed(new ActionEvent(this,
                            ActionEvent.ACTION_FIRST, "Login Succeeded!"));

                } catch (AuthenticationException ae) {

                    // Bad username and/or password

                    fireActionPerformed(new ActionEvent(this,
                            ActionEvent.ACTION_FIRST, "Login Failed!"));

                    showErrorBox("Invalid username and/or password", "Unable to log in");

                    password.setText("");

                } catch (UnableToConnectException ue) {

                    // Server didn't connect for some reason, probably because
                    // it wasn't running

                    fireActionPerformed(new ActionEvent(this,
                            ActionEvent.ACTION_FIRST, "Login Failed!"));

                    showErrorBox("Unable to connect to the server. Double-check that the server is running.", "Unable to Connect");

                }	catch (final Exception e) {


                    fireActionPerformed(new ActionEvent(this,
                            ActionEvent.ACTION_FIRST, "Login Failed!"));
                    e.printStackTrace();

                }
            }
        };

        KeyListener k = new KeyAdapter() {

            public void keyPressed(final KeyEvent arg0) {

                if (arg0.getKeyChar() == KeyEvent.VK_ENTER) {
                    loginTask.run();
                }

            }
        };

        username.addKeyListener(k);
        password.addKeyListener(k);
        serverIP.addKeyListener(k);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                loginTask.run();
            }
        });

        setLayout(new GridLayout(4, 2));

        add(serverLabel);
        add(serverIP);
        add(usernameLabel);
        add(username);
        add(passwordLabel);
        add(password);
        add(loginButton);

        // TODO: Remove this before real demo
        populateLogin();
    }

    /**
     * Populate login information for testing purposes
     */
    private void populateLogin() {
        serverIP.setText("localhost:9040");
        username.setText("Chris");
        password.setText("pass4");
    }

    protected void fireActionPerformed(final ActionEvent e) {
        for (ActionListener l : listeners) {
            l.actionPerformed(e);
        }
    }

    public void addActionListener(final ActionListener listener) {
        listeners.add(listener);
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void clearFields() {
        // TODO Auto-generated method stub
        password.setText("");
        username.setText("");

    }

    private void showErrorBox(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

}
