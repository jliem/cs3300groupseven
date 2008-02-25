package colab.client.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import colab.client.ClientChatChannel;
import colab.client.ColabClient;
import colab.common.channel.ChannelDescriptor;
import colab.common.exception.ConnectionDroppedException;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import colab.common.remote.server.ConnectionInterface;

class ColabClientGUI extends JFrame {

	private final ColabClient client;

	private final LoginPanel loginPanel;
	private final FixedSizePanel loginPanelWrapper;
	private ChooseCommunityPanel communityPanel;
	private final FixedSizePanel communityPanelWrapper;
	private ChannelManagerPanel channelPanel;
	private JPanel activePanel;
	private UserName currentUser;

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
					client.loginCommmunity(communityPanel
							.getCurrentCommunityName());
				} catch (final RemoteException re) {
					// The christopher martin experience: enjoy!
				}

				channelPanel = new ChannelManagerPanel(client.getChannels());

				gotoChannelView();

				channelPanel.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						ChannelDescriptor cd;
						
						if (e.getActionCommand().equalsIgnoreCase("Logout!")) {
							try {
								client.logOutUser();
								gotoUserLoginView(true);

							} catch (ConnectionDroppedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}
						
						else if(e.getActionCommand().equalsIgnoreCase("Change!")){
							try {
								client.logOutCommunity();
							} catch (ConnectionDroppedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							gotoCommunityLoginView();							
						}
								
								

						while ((cd = channelPanel.dequeueJoinedChannel()) != null) {
							try {
								// TODO: this is a hack, rewrite with more protocol handling
								ChatChannelFrame f = new ChatChannelFrame(
										(ClientChatChannel) client
												.joinChannel(cd), currentUser);
								f.setVisible(true);
							} catch (RemoteException ex) {
								System.err.println(ex);
							}
						}
					}
				});
			}
		});

	}

	private void gotoUserLoginView(boolean logout) {

		if(logout)
			loginPanel.clearFields();
		
		setActivePanel(loginPanelWrapper);
		setTitle("CoLab Login");
		setResizable(false);
		setSize(500, 200);
		loginPanel.updateUI();

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

		this.currentUser = new UserName(loginPanel.getCurrentUser());
		communityPanel.setCommunityNames(commNames.toArray());
		setActivePanel(communityPanelWrapper);
		setTitle("Select Community");
		setResizable(false);
		setSize(500, 200);
		communityPanel.updateUI();

	}

	private void gotoChannelView() {
		channelPanel = new ChannelManagerPanel(client.getChannels());
		setActivePanel(channelPanel);
		this.setJMenuBar(channelPanel.getMenuBar());
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
