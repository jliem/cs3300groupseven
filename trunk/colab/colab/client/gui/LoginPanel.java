package colab.client.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import colab.common.exception.IncorrectPasswordException;
import colab.common.exception.NetworkException;
import colab.common.exception.UnableToConnectException;
import colab.common.exception.UserAlreadyExistsException;
import colab.common.exception.UserDoesNotExistException;
import colab.common.naming.UserName;

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
                    client.loginUser(
                            username.getText(),
                            password.getPassword(),
                            serverIP.getText());

                    fireActionPerformed(new ActionEvent(this,
                            ActionEvent.ACTION_FIRST, "Login Succeeded!"));

                } catch (final UserDoesNotExistException e) {

                    boolean createUser = showYesNoDialog(
                            "The specified user account does not exist.\n\n"
                            + "Do you want to create it now?",
                            "User does not exist");

                    if (createUser) {

                        try {
                            client.createUser(
                                new UserName(username.getText()),
                                password.getPassword());
                        } catch (final UserAlreadyExistsException uaee) {
                            showErrorBox("This user name already exists.",
                                    "Failed to create user");
                            return;
                        } catch (final NetworkException networkException) {
                            // <(^.^)>
                            return;
                        }

                        // Run the task again to log in (it had better work
                        // this time - be careful of infinite looping!)
                        run();

                    }

                } catch (final IncorrectPasswordException e) {

                    // Bad username and/or password

                    fireActionPerformed(new ActionEvent(this,
                            ActionEvent.ACTION_FIRST, "Login Failed!"));

                    showErrorBox("Invalid password", "Unable to log in");

                    password.setText("");

                } catch (final UnableToConnectException ue) {

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

        // Select the entire text box when clicking the username or password
        // field
        FocusListener f = new FocusAdapter() {
            public void focusGained(FocusEvent fe) {
                // getText is deprecated for JPasswordField, so we
                // need two if cases
                if (fe.getSource() == password) {
                    password.setSelectionStart(0);
                    password.setSelectionEnd(password.getPassword().length);
                } else if (fe.getSource() == username) {
                    username.setSelectionStart(0);
                    username.setSelectionEnd(username.getText().length());
                }
            }
        };

        username.addFocusListener(f);
        password.addFocusListener(f);

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

    private void showErrorBox(final String message, final String title) {
        JOptionPane.showMessageDialog(
                this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private boolean showYesNoDialog(final String message, final String title) {
        int selection = JOptionPane.showConfirmDialog(
                this, message, title, JOptionPane.YES_NO_OPTION);
        return selection == JOptionPane.YES_OPTION;
    }

}
