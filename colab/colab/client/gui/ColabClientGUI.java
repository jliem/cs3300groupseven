package colab.client.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import colab.client.ColabClient;
import colab.common.channel.ChannelDescriptor;
import colab.common.naming.CommunityName;
import colab.common.remote.server.ConnectionInterface;

class ColabClientGUI extends JFrame {

    private final ColabClient client;

    private final LoginPanel loginPanel;
    private final FixedSizePanel loginPanelWrapper;
    private ChooseCommunityPanel communityPanel;
    private final FixedSizePanel communityPanelWrapper;
    private ChannelManagerPanel channelPanel;
    private JPanel activePanel;
    private String currentUser;

    public ColabClientGUI(final ColabClient client) {
        this.client = client;

        this.loginPanel = new LoginPanel(client);
        this.loginPanelWrapper = new FixedSizePanel(loginPanel, new Dimension(
                420, 120));

        this.communityPanel = new ChooseCommunityPanel();
        this.communityPanelWrapper = new FixedSizePanel(communityPanel,
                new Dimension(420, 120));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        gotoUserLoginView(false);

        loginPanel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (e.getActionCommand().equals("Login Succeeded!")) {
                    gotoCommunityLoginView();
                }
            }
        });

        communityPanel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    client.loginCommmunity(
                            communityPanel.getCurrentCommunityName());
                } catch (final RemoteException re) {
                    // The christopher martin experience: enjoy!
                }

                channelPanel = new ChannelManagerPanel(client.getChannels());

                gotoChannelView();

                channelPanel.addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent e) {
                        ChannelDescriptor cd;
                        while((cd = channelPanel.dequeueJoinedChannel()) != null) {
                            try {
                                client.joinChannel(cd);
                            } catch(RemoteException ex) {
                                // The christopher martin experience: enjoy!
                            }
                        }
                    }
                });
            }
        });

    }

    private void gotoUserLoginView(boolean logout) {

        if (!logout) {
            setActivePanel(loginPanelWrapper);
            setTitle("CoLab Login");
            setResizable(false);
            setSize(500, 200);
            loginPanel.updateUI();
        } else {
            loginPanel.clearFields();
            setActivePanel(loginPanelWrapper);
            setTitle("CoLab Login");
            setResizable(false);
            setSize(500, 200);
            loginPanel.updateUI();
        }

    }

    private void gotoCommunityLoginView() {

        ArrayList<String> commNames = new ArrayList<String>();
        try {
            for (CommunityName name : client.getMyCommunityNames()) {
                commNames.add(name.getValue());
            }
        } catch (RemoteException re) {
            re.printStackTrace();
        }

        this.currentUser = loginPanel.getCurrentUser();
        communityPanel.setCommunityNames(commNames.toArray());
        setActivePanel(communityPanelWrapper);
        setTitle("Select Community");
        setResizable(false);
        setSize(500, 200);
        communityPanel.updateUI();

    }

    private void gotoChannelView() {
        setActivePanel(channelPanel);
        setTitle("");
        setResizable(false);
        setSize(120, 300);
        channelPanel.updateUI();
    }

    private void setActivePanel(final JPanel newActivePanel) {
        if (activePanel != null) {
            remove(activePanel);
        }
        activePanel = newActivePanel;
        add(activePanel);
    }

    public ColabClientGUI() throws RemoteException {
        this(new ColabClient());
    }

    public void logout() {

        // Try to close the connection
        ConnectionInterface connection = client.getConnection();

        if (connection != null) {
            try {
                connection.logOutUser();
            } catch (RemoteException re) {
                re.printStackTrace();
            }
        }


        gotoUserLoginView(true);
    }

    public void switchCommunity() {
        // Try to log out on the server
        ConnectionInterface connection = client.getConnection();

        if (connection != null) {
            try {
                connection.logOutCommunity();
            } catch (RemoteException re) {
                re.printStackTrace();
            }
        }

        gotoCommunityLoginView();
    }

    public static void main(final String[] args) throws RemoteException {

        // Assign a security manager, in the event
        // that dynamic classes are loaded.
        // if (System.getSecurityManager() == null) {
        // System.setSecurityManager(new RMISecurityManager());
        // }

        new ColabClientGUI();

    }

}
