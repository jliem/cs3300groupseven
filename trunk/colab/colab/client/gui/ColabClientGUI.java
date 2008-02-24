package colab.client.gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JFrame;

import colab.client.ColabClient;

public class ColabClientGUI extends JFrame {

    private final ColabClient client;

    private final LoginPanel loginPanel;
    private final ChooseCommunityPanel commPanel;

    public ColabClientGUI(final ColabClient client) {
        super();

        this.client = client;

        this.loginPanel = new LoginPanel(client);
        this.commPanel = new ChooseCommunityPanel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        gotoUserLoginView();

        loginPanel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (e.getActionCommand().equals("Login Succeeded!")) {
                    gotoCommunityLoginView();
                }
            }
        });

    }

    private void gotoUserLoginView() {
        removeAll();
        setTitle("CoLab Login");
        setResizable(false);
        setSize(400, 100);
        add(loginPanel);
        loginPanel.updateUI();
    }

    private void gotoCommunityLoginView() {
        removeAll();
        setTitle("Select Community");
        setResizable(false);
        setSize(400, 100);
        add(commPanel);
        commPanel.updateUI();
    }

    public ColabClientGUI() throws RemoteException {
        this(new ColabClient());
    }

    public static void main(final String[] args) throws RemoteException {

        // Assign a security manager, in the event
        // that dynamic classes are loaded.
        //if (System.getSecurityManager() == null) {
        //    System.setSecurityManager(new RMISecurityManager());
        //}

        new ColabClientGUI();

    }


}
