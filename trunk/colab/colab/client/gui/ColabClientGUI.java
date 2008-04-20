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

import colab.client.ClientChannel;
import colab.client.ColabClient;
import colab.client.gui.login.ChooseCommunityPanel;
import colab.client.gui.login.LoginPanel;
import colab.client.gui.login.NewCommunityDialog;
import colab.common.DebugManager;
import colab.common.Logger;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.type.ChannelType;
import colab.common.event.channel.ChannelEvent;
import colab.common.event.channel.ChannelListener;
import colab.common.exception.AuthenticationException;
import colab.common.exception.CommunityDoesNotExistException;
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

    private ArrayList<ClientChannelFrame> channelWindows;

    private ChannelManagerPanel channelPanel;

    private final ChannelWindowListener channelWindowListener;

    private JPanel activePanel;

    private UserName currentUser;

    private JMenuBar menuBar;

    private JMenu fileMenu;

    private JMenuItem logoutItem;

    private JMenuItem changeCommItem;

    private JMenuItem quitItem;

    private JMenu adminMenu;

    private JMenuItem commSettingsItem;

    private CommunityName communityName;

    public ColabClientGUI(final ColabClient client) {

        Image icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                "logo.png")).getImage();
        this.setIconImage(icon);

        if (client == null) {
            throw new IllegalArgumentException(
                    "Cannot create ColabClientGUI with "
                    + "a null ColabClient");
        }

        this.client = client;


        channelPanel = new ChannelManagerPanel(client);

        channelPanel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                ChannelDescriptor cd;

                if (e.getActionCommand()
                        .equalsIgnoreCase("Logout!")) {
                    try {
                        client.logOutUser();
                        gotoUserLoginView(true);

                    } catch (ConnectionDroppedException e1) {
                        handleConnectionDropped();
                        DebugManager.connectionDropped(e1);
                    }

                } else if (e.getActionCommand().equalsIgnoreCase(
                        "Change!")) {
                    try {
                        client.logOutCommunity();
                    } catch (ConnectionDroppedException e1) {
                        handleConnectionDropped();
                        DebugManager.connectionDropped(e1);
                    }
                    gotoCommunityLoginView();
                }

                while ((cd = channelPanel.dequeueJoinedChannel())
                        != null) {

                    try {
                        handleJoinChannel(cd);
                    } catch (RemoteException re) {
                        DebugManager.exception(re);
                    }
                }
            }
        });

        // Listen for channel changes
        client.addChannelListener(new ChannelListener() {
            public void handleChannelEvent(final ChannelEvent event) {
                channelPanel.refreshChannels();
            }
        });

        this.loginPanel = new LoginPanel(client);
        this.loginPanelWrapper = new FixedSizePanel(loginPanel, new Dimension(
                420, 120));

        this.communityPanel = new ChooseCommunityPanel(client);
        this.communityPanelWrapper = new FixedSizePanel(communityPanel,
                new Dimension(420, 150));

        channelWindows = new ArrayList<ClientChannelFrame>();

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        logoutItem = new JMenuItem("Logout");
        changeCommItem = new JMenuItem("Change Communities");
        quitItem = new JMenuItem("Quit");

        adminMenu = new JMenu("Admin");
        commSettingsItem = new JMenuItem("Community Settings");
        adminMenu.add(commSettingsItem);

        ActionListener menuListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {

                if (e.getSource() == logoutItem) {
                    gotoUserLoginView(true);
                    try {
                        closeOpenedChannelWindows();
                        client.logOutUser();
                    } catch (ConnectionDroppedException e1) {
                        handleConnectionDropped();
                        DebugManager.connectionDropped(e1);
                    }
                } else if (e.getSource() == changeCommItem) {
                    try {
                        closeOpenedChannelWindows();
                        client.logOutCommunity();
                    } catch (ConnectionDroppedException e1) {
                        handleConnectionDropped();
                        DebugManager.connectionDropped(e1);
                    }
                    gotoCommunityLoginView();
                } else if (e.getSource() == quitItem) {
                    client.exitProgram();
                } else if (e.getSource() == commSettingsItem) {
                    if (communityName != null) {
                        CommunitySettingsDialog dialog =
                            new CommunitySettingsDialog(client,
                                    communityName, currentUser);
                        dialog.pack();
                        dialog.setVisible(true);
                    }
                }

            }
        };

        logoutItem.addActionListener(menuListener);
        changeCommItem.addActionListener(menuListener);
        quitItem.addActionListener(menuListener);
        commSettingsItem.addActionListener(menuListener);

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

                if (event.getActionCommand()
                        .equalsIgnoreCase("Community Chosen")) {

                    handleCommunityChosen();

                } else if (event.getActionCommand()
                        .equalsIgnoreCase("New Community")) {


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

        channelWindowListener = new ChannelWindowListener();

    }

    /**
     * Display window for creating a new community.
     */
    private void handleNewCommunity() {
        NewCommunityDialog frame = new NewCommunityDialog(
                communityPanel, client);
        frame.pack();
        frame.setVisible(true);
        frame.setPreferredSize(new Dimension(400, 800));
    }

    /**
     * Closes cleanly when connection is dropped.
     */
    private void handleConnectionDropped() {
        JOptionPane.showMessageDialog(activePanel,
                "The connection to the server was lost. "
                + "Double-check that the server is running.",
                "Connection to Server Lost",
                JOptionPane.ERROR_MESSAGE);

        // On error, return to login screen and pray that the server will
        // be restarted into a clean state

        // TODO If the server is not restarted, we could get
        // into an inconsistent state when we try to log back in.
        this.gotoUserLoginView(true);
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
            DebugManager.network(ne);
        } catch (RemoteException re) {
            DebugManager.exception(re);
        }

        // Exit method if an exception occurred while
        // logging in
        if (!loginOK) {
            return;
        }

        this.gotoChannelView();
    }

    /**
     * Creates a new channel and joins it.
     *
     * @param desc channel descriptor
     * @throws RemoteException if an rmi error occurs
     */
    private void handleJoinChannel(final ChannelDescriptor desc)
        throws RemoteException {

        // Check whether we've already joined this channel
        ClientChannelFrame existing = this.getChannelFrame(desc);

        if (existing == null) {
            ClientChannelFrame f = createNewChannelFrame(desc);

            f.addWindowListener(channelWindowListener);
            f.pack();
            f.setVisible(true);
            channelWindows.add(f);
        } else {
            // There is already a window open for this channel.
            // Restore the window (if it's not minimized, this has
            // no effect)
            existing.setExtendedState(JFrame.NORMAL);

            // Give focus to the window
            existing.setVisible(true);
        }
    }

    /**
     * Helper method to create a new ClientChannelFrame.
     * @param desc the descriptor
     * @return a new ClientChannelFrame
     * @throws RemoteException if an rmi error occurs
     */
    private ClientChannelFrame createNewChannelFrame(
            final ChannelDescriptor desc) throws RemoteException {

        ClientChannel channel = client.joinChannel(desc);

        ChannelType type = desc.getType();
        ClientChannelFrame frame = type.createClientChannelFrame(
                client, channel, currentUser);

        return frame;
    }

    /**
     * Retrieves the ClientChannelFrame with a matching ChannelDescriptor.
     *
     * @param desc the channel descriptor
     * @return a reference to the open ClientChannelFrame, or null
     * if none existed.
     */
    private ClientChannelFrame getChannelFrame(final ChannelDescriptor desc) {

        for (ClientChannelFrame f : channelWindows) {
            if (f.getChannel().getChannelDescriptor().equals(desc)) {
                return f;
            }
        }

        return null;
    }

    /**
     * Displays GUI to ask user for password to join community.
     *
     * @return the String the user entered as a password, or null if
     * the user canceled.
     */
    private char[] promptForCommunityPassword() {
        String result = JOptionPane.showInputDialog(this,
                "You are not a member of the community you selected.\n\nPlease"
                + " enter the community password to log in:",
                "Enter community password");

        if (result != null) {
            return result.toCharArray();
        }

        return null;
    }

    /**
     * Attempts to close any windows opened by this frame.
     */
    private void closeOpenedChannelWindows() {
        // channelWindows should contain only windows that are still open
        for (ClientChannelFrame channelFrame : channelWindows) {
            try {
                if (channelFrame != null) {
                    channelFrame.exit();
                }
            } catch (final Exception e) {
                DebugManager.windowClose(e);
            }
        }



        // Reset the list
        channelWindows.clear();
    }

    /**
     * Clean up and exit the program.
     */
    private void exit() {
        // First close all opened windows
        closeOpenedChannelWindows();

        try {
            // Log the user out. This will also
            // log out of any communities and channels.
            if (client.hasUserLogin()) {
                client.logOutUser();
            }

            // Exceptions are expected if we weren't in the proper state
            // to log out (ex. a window was closed, and then we try to close
            // the parent which triggers another logoff)

        } catch (final Exception e) {
            DebugManager.windowClose(e);
        }
    }

    private void gotoUserLoginView(final boolean logout) {

        menuBar.removeAll();

        fileMenu.removeAll();
        fileMenu.add(quitItem);
        if (logout) {
            loginPanel.clearFields();
        }

        menuBar.add(fileMenu);

        setActivePanel(loginPanelWrapper);
        setTitle("CoLab Login");
        setResizable(false);
        setSize(500, 200);
        loginPanel.updateUI();

    }

    private void gotoCommunityLoginView() {

        menuBar.removeAll();
        fileMenu.removeAll();
        fileMenu.add(logoutItem);
        fileMenu.add(quitItem);

        menuBar.add(fileMenu);

        communityPanel.refreshCommunityNames();

        this.currentUser = new UserName(loginPanel.getCurrentUser());

        setActivePanel(communityPanelWrapper);
        setTitle("Select Community");
        setResizable(false);
        setSize(500, 230);
        communityPanel.updateUI();

    }

    private void gotoChannelView() {

        menuBar.removeAll();

        fileMenu.removeAll();
        fileMenu.add(changeCommItem);
        fileMenu.add(logoutItem);
        fileMenu.add(quitItem);

        menuBar.add(fileMenu);

            if (communityName != null) {
                try {
                    if (client.isModerator(communityName)) {
                        menuBar.add(adminMenu);
                    }
                } catch (RemoteException e) {
                    DebugManager.remote(e);
                } catch (CommunityDoesNotExistException e) {
                    DebugManager.exception(e);
                }
            }

        channelPanel.refreshChannels();

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
     * @throws ConnectionDroppedException if the connection is dropped
     */
    public void logout() throws ConnectionDroppedException {

        client.logOutUser();

        gotoUserLoginView(true);

    }

    /**
     * Logs out of the community and moves into the
     * community selection view.
     * @throws ConnectionDroppedException if the connection is dropped
     */
    public void switchCommunity() throws ConnectionDroppedException {

        client.logOutCommunity();

        gotoCommunityLoginView();

    }

    private void showErrorBox(final String message, final String title) {
        JOptionPane.showMessageDialog(this, message, title,
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(final String[] args) throws RemoteException {

        DebugManager.enableExceptions(true);

        Logger.enable(true);

        new ColabClientGUI();

    }

    /**
     * A listener for all ClientChannelFrames that are opened.
     * This listener will have no effect if it's attached to
     * something that isn't a ClientChannelFrame.
     *
     * @author JL
     *
     */
    private class ChannelWindowListener extends WindowAdapter {
        public void windowClosing(final WindowEvent event) {

            // Find the source frame
            Object source = event.getSource();

            // Only attempt removal if it was actually
            // a ClientChannelFrame
            if (source instanceof ClientChannelFrame) {
                boolean removed = channelWindows.remove(source);

                if (!removed) {
                    throw new IllegalStateException("A ClientChannelFrame "
                            + "just closed, but it was not "
                             + "in ColabClientGUI's list of open windows.");
                }
            }
        }
    }

}
