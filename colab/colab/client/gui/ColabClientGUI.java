package colab.client.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import colab.client.ClientChatChannel;
import colab.client.ColabClient;
import colab.common.DebugManager;
import colab.common.channel.ChannelDescriptor;
import colab.common.exception.AuthenticationException;
import colab.common.exception.ConnectionDroppedException;
import colab.common.exception.NetworkException;
import colab.common.exception.UserAlreadyLoggedInException;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;

class ColabClientGUI extends JFrame {

    /** Serialization verson number. */
    public static final long serialVersionUID = 1L;

    private final ColabClient client;

    private final LoginPanel loginPanel;

    private final FixedSizePanel loginPanelWrapper;

    private ChooseCommunityPanel communityPanel;

    private final FixedSizePanel communityPanelWrapper;

    private ArrayList<ClientChannelFrame> channelList;

    private ChannelManagerPanel channelPanel;

    private JPanel activePanel;

    private UserName currentUser;

    private JMenuBar menuBar;

    private JMenu menu;

    private JMenuItem logoutItem;

    private JMenuItem changeCommItem;

    private JMenuItem quitItem;

    private CommunityName communityName;

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

        channelList = new ArrayList<ClientChannelFrame>();

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
                        leaveOpenedChannels();
                        client.logOutUser();
                    } catch (ConnectionDroppedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }

                if (e.getSource() == changeCommItem) {
                    try {
                        leaveOpenedChannels();
                        client.logOutCommunity();
                    } catch (ConnectionDroppedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    gotoCommunityLoginView();
                }

                if (e.getSource() == quitItem) {
                    client.exitProgram();
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
            public void actionPerformed(final ActionEvent event) {

                if (event.getActionCommand().equalsIgnoreCase("Community Chosen")) {
                    handleCommunityChosen();
                } else if (event.getActionCommand().equalsIgnoreCase("New Community")) {
                    handleNewCommunity();
                }
            }
        });

        // If this frame is closed, exit the channel and clean up
        addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                exit();
            }
        });
    }

    /**
     * Display window for creating a new community.
     */
    private void handleNewCommunity() {
        NewCommunityFrame frame = new NewCommunityFrame(communityPanel, client);
        frame.pack();
        frame.setVisible(true);
        frame.setPreferredSize(new Dimension(400, 800));
    }

    /**
     * Code that runs when a user attempts to log into
     * a community.
     */
    private void handleCommunityChosen() {

        boolean loginOK = false;

        try {
            communityName = communityPanel
                    .getCurrentCommunityName();

            char[] password = null;

            if (!client.isMember(communityName)) {
                password = promptForCommunityPassword();
            }

            client.loginCommunity(communityName, password);

            loginOK = true;

        } catch (final UserAlreadyLoggedInException ualie) {
            showErrorBox(
                    "You are already logged into this community, "
                            + "possibly at another location.",
                    "Unable to log in");
        } catch (final AuthenticationException ae) {
            showErrorBox(
                    "You are unauthorized to join this community.",
                    "Unable to join community");
        } catch (final NetworkException ne) {

            if (DebugManager.NETWORK)
                ne.printStackTrace();
        } catch (RemoteException re) {

            if (DebugManager.EXCEPTIONS)
                re.printStackTrace();
        }

        // Exit method if an exception occurred while
        // logging in
        if (!loginOK)
            return;

        channelPanel = new ChannelManagerPanel(client);

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
                        channelList.add(f);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Displays GUI to ask user for password to join community.
     *
     * @return the String the user entered as a password, or null if
     * the user canceled.
     */
    private char[] promptForCommunityPassword() {
        String result = JOptionPane.showInputDialog(this,
                "You are not a member of the community you selected.\n\nPlease" +
                " enter the community password to log in:",
                "Enter community password");

        if (result != null)
            return result.toCharArray();

        return null;
    }

    /**
     * Attempts to close any windows opened by this frame.
     */
    private void leaveOpenedChannels() {
        // Because the windows that were opened from this frame might
        // have been closed since then, channelList may have invalid entries
        // for windows which no longer exist
        for (ClientChannelFrame channelFrame : channelList) {
            try {
                // TODO: Think of some better way of detecting this
                if (channelFrame.isVisible()) {
                    channelFrame.exit();
                }
            } catch (Exception e) {
                // TODO: Handle this?
                // Probably don't need to worry about it
                // as the window is closing anyway
                if (DebugManager.EXIT);
                    e.printStackTrace();
            }
        }

        // Reset the list
        channelList.clear();
    }

    /**
     * Clean up and exit the program.
     */
    private void exit() {
        // First attempt to exit any channels this frame has opened
        leaveOpenedChannels();

        try {
            // Log the user out. This will also
            // log out of any communities.
            if (client.hasUserLogin())
                client.logOutUser();

            // Exceptions are expected if we weren't in the proper state
            // to log out (ex. a window was closed, and then we try to close
            // the parent which triggers another logoff)

            // TODO: Think of a better way of handling such situations
        } catch (NullPointerException ne) {
            if (DebugManager.EXIT)
                ne.printStackTrace();
        } catch (Exception e) {
            if (DebugManager.EXIT)
                e.printStackTrace();
        }
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

        communityPanel.refreshCommunityNames();

        this.currentUser = new UserName(loginPanel.getCurrentUser());

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
        channelPanel = new ChannelManagerPanel(client);
        setActivePanel(channelPanel);
        if (communityName == null) {
            setTitle("");
        } else {
            setTitle(communityName.getValue());
        }

        //setResizable(false);
        setResizable(true);
        setSize(300, 330);
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
