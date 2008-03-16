package colab.client.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import colab.client.ClientChatChannel;
import colab.client.ColabClient;
import colab.common.channel.ChannelDescriptor;
import colab.common.exception.AuthenticationException;
import colab.common.exception.ConnectionDroppedException;
import colab.common.exception.NetworkException;
import colab.common.exception.UserAlreadyLoggedInException;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;

class ColabClientGUI extends JFrame {

    private final ColabClient client;

    private final LoginPanel loginPanel;
    private final FixedSizePanel loginPanelWrapper;
    private ChooseCommunityPanel communityPanel;
    private final FixedSizePanel communityPanelWrapper;
    private ChannelManagerPanel channelPanel;
    private JPanel activePanel;
    private UserName currentUser;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem logoutItem, changeCommItem, quitItem;

    public ColabClientGUI(final ColabClient client) {

        Image icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                "logo.png")).getImage();
        this.setIconImage(icon);

        this.client = client;

        this.loginPanel = new LoginPanel(client);
        this.loginPanelWrapper = new FixedSizePanel(loginPanel, new Dimension(
                420, 120));

        this.communityPanel = new ChooseCommunityPanel(client);
        this.communityPanelWrapper = new FixedSizePanel(communityPanel,
                new Dimension(420, 120));

        menuBar = new JMenuBar();
        menu = new JMenu("File");
        menuBar.add(menu);
        logoutItem = new JMenuItem("Logout");
        changeCommItem = new JMenuItem("Change Communities");
        quitItem = new JMenuItem("Quit");

        ActionListener menuListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {

                if (e.getSource() == logoutItem) {
                    gotoUserLoginView(true);
                    try {
                        client.logOutUser();
                    } catch (ConnectionDroppedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }

                if (e.getSource() == changeCommItem) {
                    try {
                        client.logOutCommunity();
                    } catch (ConnectionDroppedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    gotoCommunityLoginView();
                }

                if (e.getSource() == quitItem) {
                    System.exit(1);
                }

            }
        };

        logoutItem.addActionListener(menuListener);
        changeCommItem.addActionListener(menuListener);
        quitItem.addActionListener(menuListener);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        gotoUserLoginView(false);
        this.setJMenuBar(menuBar);

        loginPanel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (e.getActionCommand().equals("Login Succeeded!")) {
                    gotoCommunityLoginView();
                }
            }
        });

        communityPanel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {

                if (e.getActionCommand().equalsIgnoreCase("Community Chosen")) {
                    try {
                        CommunityName communityName = communityPanel
                                .getCurrentCommunityName();
                        client.loginCommmunity(communityName);
                    } catch (final UserAlreadyLoggedInException ualie) {
                        showErrorBox(
                                "You are already logged into this community, "
                                        + "possibly at another location.",
                                "Unable to log in");
                        return;
                    } catch (final AuthenticationException ae) {
                        // The christopher martin experience: enjoy!
                    } catch (final NetworkException re) {
                        // The christopher martin experience: enjoy!
                    }

                    Vector<ChannelDescriptor> channels = client.getChannels();
                    channelPanel = new ChannelManagerPanel(channels);

                    gotoChannelView();

                    channelPanel.addActionListener(new ActionListener() {
                        public void actionPerformed(final ActionEvent e) {
                            ChannelDescriptor cd;

                            if (e.getActionCommand()
                                    .equalsIgnoreCase("Logout!")) {
                                try {
                                    client.logOutUser();
                                    gotoUserLoginView(true);

                                } catch (ConnectionDroppedException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }

                            } else if (e.getActionCommand().equalsIgnoreCase(
                                    "Change!")) {
                                try {
                                    client.logOutCommunity();
                                } catch (ConnectionDroppedException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                                gotoCommunityLoginView();
                            }

                            while ((cd = channelPanel.dequeueJoinedChannel())
                                    != null) {
                                try {
                                    // TODO: this is a hack
                                    // rewrite with more protocol handling
                                    ChatChannelFrame f = new ChatChannelFrame(
                                            client, (ClientChatChannel) client
                                                    .joinChannel(cd),
                                            currentUser);
                                    f.setVisible(true);
                                } catch (RemoteException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    });
                }

                if(e.getActionCommand().equalsIgnoreCase("New Community")){


                }
            }
        });

    }

    private void gotoUserLoginView(final boolean logout) {

        menu.removeAll();
        menu.add(quitItem);
        if (logout) {
            loginPanel.clearFields();
        }

        setActivePanel(loginPanelWrapper);
        setTitle("CoLab Login");
        setResizable(false);
        setSize(500, 200);
        loginPanel.updateUI();

    }

    private void gotoCommunityLoginView() {

        menu.removeAll();
        menu.add(logoutItem);
        menu.add(quitItem);
        ArrayList<String> commNames = new ArrayList<String>();
        try {
            for (CommunityName name : client.getMyCommunityNames()) {
                commNames.add(name.getValue());
            }
        } catch (RemoteException re) {
            re.printStackTrace();
        }

        this.currentUser = new UserName(loginPanel.getCurrentUser());
        communityPanel.setCommunityNames(commNames.toArray());
        setActivePanel(communityPanelWrapper);
        setTitle("Select Community");
        setResizable(false);
        setSize(500, 200);
        communityPanel.updateUI();

    }

    private void gotoChannelView() {

        menu.add(changeCommItem);
        menu.add(logoutItem);
        menu.add(quitItem);
        channelPanel = new ChannelManagerPanel(client.getChannels());
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

    /**
     * Constructs a new client gui.
     *
     * @throws RemoteException if an rmi error occurs
     */
    public ColabClientGUI() throws RemoteException {
        this(new ColabClient());
    }

    /**
     * Logs out and returns to the user login view.
     */
    public void logout() {

        try {
            client.logOutUser();
        } catch (final ConnectionDroppedException e) {
            e.printStackTrace();
        }

        gotoUserLoginView(true);

    }

    /**
     * Logs out of the community and moves into the
     * community selection view.
     */
    public void switchCommunity() {

        try {
            client.logOutCommunity();
        } catch (final ConnectionDroppedException e) {
            e.printStackTrace();
        }

        gotoCommunityLoginView();

    }

    private void showErrorBox(final String message, final String title) {
        JOptionPane.showMessageDialog(this, message, title,
                JOptionPane.ERROR_MESSAGE);
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
