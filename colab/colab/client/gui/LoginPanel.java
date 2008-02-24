package colab.client.gui;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import colab.client.ColabClient;

public class LoginPanel extends JPanel {

    /** Serialization version number. */
    private static final long serialVersionUID = 1L;

    private JLabel usernameLabel, passwordLabel, serverLabel;
    private JPasswordField password;
    private JTextField username, serverIP;
    private JButton loginButton;
    protected boolean loginOK;

    private ArrayList<ActionListener> listeners;

    private static int eventID = 1;

    public LoginPanel(final ColabClient client)
    {
        usernameLabel = new JLabel("Username / Desired Username: ");
        passwordLabel = new JLabel("Password / Desired Password: ");
        serverLabel = new JLabel("Enter server IP: ");
        username = new JTextField(12);
        password = new JPasswordField(12);
        serverIP = new JTextField(32);
        loginButton = new JButton("Submit");

        listeners = new ArrayList<ActionListener>();



        KeyListener k = new KeyListener()
        {

            public void keyPressed(KeyEvent arg0) {
                // TODO Auto-generated method stub
                if(arg0.getKeyChar() == KeyEvent.VK_ENTER){
                    if(client.loginUser(username.getText(), password.getPassword(), serverIP.getText()))
                        fireActionPerformed(new ActionEvent(this, eventID++, "Login Succeeded!"));
                    else
                        fireActionPerformed(new ActionEvent(this, eventID++, "Login Failed!"));
                }
            }

            public void keyReleased(KeyEvent arg0) {
                // TODO Auto-generated method stub

            }

            public void keyTyped(KeyEvent arg0) {
                // TODO Auto-generated method stub


            }
        };

        username.addKeyListener(k);
        password.addKeyListener(k);
        serverIP.addKeyListener(k);


        loginButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(client.loginUser(username.getText(), password.getPassword(), serverIP.getText()))
                    fireActionPerformed(new ActionEvent(this, eventID++, "Login Succeeded!"));
                else
                    fireActionPerformed(new ActionEvent(this, eventID++, "Login Failed!"));
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


    }

    protected void fireActionPerformed(ActionEvent e)
    {
        for(ActionListener l:listeners)
            l.actionPerformed(e);
    }

    public void addActionListener(ActionListener listener)
    {
        listeners.add(listener);
    }
}
