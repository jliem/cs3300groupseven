package colab.client.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import colab.client.ColabClient;

public class ColabClientGUI extends JFrame {

    private final ColabClient client;

    private final LoginPanel loginPanel;
    private final FixedSizePanel loginPanelWrapper;
    private final ChooseCommunityPanel communityPanel;
    private final FixedSizePanel communityPanelWrapper;

    private JPanel activePanel;

    public ColabClientGUI(final ColabClient client) {
        super();

        this.client = client;

        this.loginPanel = new LoginPanel(client);
        this.loginPanelWrapper = new FixedSizePanel(
                loginPanel, new Dimension(420, 120));

        this.communityPanel = new ChooseCommunityPanel();
        this.communityPanelWrapper = new FixedSizePanel(
                communityPanel, new Dimension(420, 120));

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
        setActivePanel(loginPanelWrapper);
        setTitle("CoLab Login");
        setResizable(false);
        setSize(500, 240);
        loginPanel.updateUI();
    }

    private void gotoCommunityLoginView() {
        setActivePanel(communityPanelWrapper);
        setTitle("Select Community");
        setResizable(false);
        setSize(500, 240);
        communityPanel.updateUI();
    }

    private void setActivePanel(final JPanel newActivePanel) {
        if (activePanel != null) {
            remove(activePanel);
        }
        add(activePanel = newActivePanel);
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
