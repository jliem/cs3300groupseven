package colab.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import colab.common.ConnectionState;
import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDescriptor;
import colab.common.naming.ChannelName;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import colab.common.remote.client.ChannelInterface;
import colab.common.remote.client.ColabClientInterface;
import colab.common.remote.exception.AuthenticationException;
import colab.common.remote.exception.CommunityDoesNotExistException;
import colab.common.remote.server.ConnectionInterface;

/**
 * Server implementation of {@link ConnectionInterface}.
 */
final class Connection extends UnicastRemoteObject
        implements ConnectionInterface {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * The server which created this connection.
     */
    private final ColabServer server;

    /**
     * A remote reference to the connected client.
     */
    private final ColabClientInterface client;

    /**
     * The current state of the connection.
     */
    private ConnectionState state;

    /**
     * The user that has logged in on this connection (if any).
     */
    private UserName username;

    /**
     * The community that has been logged into on this connection (if any).
     */
    private Community community;

    /**
     * Constructs a new Connection.
     *
     * @param server the server to which the client is connected
     * @param client a remote reference to the client object
     * @throws RemoteException if an rmi error occurs
     */
    public Connection(final ColabServer server,
            final ColabClientInterface client) throws RemoteException {

        // Keep a reference to the server and client
        this.server = server;
        this.client = client;

        // Put the connection into the initial state
        this.state = ConnectionState.CONNECTED;

    }

    /**
     * Returns the state of this connection.
     *
     * @return the state object representing the connection's login status
     */
    public ConnectionState getState() {
        return this.state;
    }

    /**
     * Returns the user that is logged in.
     *
     * Throws IllegalStateException if no user is logged in.
     *
     * @return a user that has authenticated on this connection
     */
    public UserName getUsername() {

        if (!this.state.hasUserLogin()) {
            throw new IllegalStateException("Not logged in as user");
        }

        return this.username;

    }

    /**
     * Returns the community that is active on this connection.
     * The connection must be logged in to a community.
     *
     * @return a community to which the user has joined in this session
     */
    public Community getCommunity() {

        if (!this.state.hasCommunityLogin()) {
            throw new IllegalStateException("Not logged in to a community");
        }

        return this.community;

    }

    /** {@inheritDoc} */
    public void logIn(final UserName username, final char[] password)
            throws RemoteException {

        // Must be in the Connected (not logged in) state
        if (this.state != ConnectionState.CONNECTED) {
            System.err.println("[Connection] Attempt to perform user "
                    + "login on connection in '"
                    + this.state + "' state");
            throw new IllegalStateException();
        }

        // Check the validity of login credentials
        UserManager userManager = server.getUserManager();
        userManager.checkPassword(username, password);

        System.err.println("User " + username + " logged in");

        // Advance to the next state if correct
        this.username = username;
        this.state = ConnectionState.LOGGED_IN;

    }

    /** {@inheritDoc} */
    public void logIn(final CommunityName communityName,
            final char[] password) throws RemoteException {

        // Must be in the Logged In (not yet in a community) state
        if (this.state != ConnectionState.LOGGED_IN) {
            System.err.println("[Connection] Attempt to perform community "
                    + "login on connection in '"
                    + this.state + "' state");
            throw new IllegalStateException();
        }

        // Check the validity of login credentials
        UserManager userManager = server.getUserManager();
        Community communityAttempt = userManager.getCommunity(communityName);

        if (communityAttempt == null) {
            throw new CommunityDoesNotExistException();
        }

        if (!communityAttempt.isMember(this.username)) {
            if (!communityAttempt.authenticate(this.username, password)) {
                throw new AuthenticationException();
            }
        }

        server.logIn(communityName, this.username, client);

        System.err.println("User " + this.username
                + " logged in to community " + communityName);

        // Advance to the next state if correct
        this.community = communityAttempt;
        this.state = ConnectionState.ACTIVE;

    }

    /** {@inheritDoc} */
    public void logOutUser() throws RemoteException {

        // If logged into a community, log out of it first
        if (this.state.hasCommunityLogin()) {
            logOutCommunity();
        }

        // If not logged in as a user, can't log out
        if (!this.state.hasUserLogin()) {
            System.err.println("[Connection] Attempt to perform user "
                    + "login on connection without any user login");
            throw new IllegalStateException();
        }

        // Log out of user
        this.username = null;
        this.state = ConnectionState.CONNECTED;

    }

    /** {@inheritDoc} */
    public void logOutCommunity() throws RemoteException {

        // If not logged in to a community, can't log out
        if (!this.state.hasCommunityLogin()) {
            System.err.println("[Connection] Attempt to log out of community "
                    + "when not logged in to any community");
            throw new IllegalStateException();
        }

        // Log out of community
        this.community = null;
        this.state = ConnectionState.LOGGED_IN;

    }

    /**
     * Return all channels in the currently logged in community.
     *
     * @return all the channels of the currently logged in community
     */
    /*
    public Collection<ChannelDescriptor> getChannels() {

        // Must be in the Connected (not logged in) state
        if (this.state != STATE.CONNECTED) {
            System.err.println("[Connection] Attempt to get channels "
                    + "on connection in '"
                    + this.state + "' state");
            throw new IllegalStateException();
        }

        ChannelManager cm = server.getChannelManager();
        Collection<ServerChannel> serverChannelColl =
            cm.getChannels(this.community.getId());

        // Convert to ChannelDescriptor by iterating through and building
        // a new list
        ArrayList<ChannelDescriptor> chanDescList =
            new ArrayList<ChannelDescriptor>();

        for (ServerChannel sc : serverChannelColl) {
            chanDescList.add(sc.getChannelDescriptor());
        }

        return chanDescList;
    }
*/

    /** {@inheritDoc} */
    public Collection<CommunityName> getAllCommunityNames()
            throws RemoteException {

        Collection<Community> communities = getAllCommunities();

        Collection<CommunityName> communityNames =
            new ArrayList<CommunityName>(communities.size());

        for (Community c : communities) {
            communityNames.add(c.getId());
        }

        return communityNames;

    }

    /** {@inheritDoc} */
    public Collection<CommunityName> getMyCommunityNames()
            throws RemoteException {

        if (!this.state.hasUserLogin()) {
            throw new IllegalStateException("Not logged in as user");
        }

        Collection<Community> communities = getAllCommunities();

        Collection<CommunityName> communityNames =
            new ArrayList<CommunityName>(communities.size());

        for (Community c : communities) {
            if (c.getMembers().contains(this.username)) {
                communityNames.add(c.getId());
            }
        }

        return communityNames;

    }


    /** {@inheritDoc} */
    public void joinChannel(final ChannelInterface clientChannel,
            final ChannelDescriptor channelDescriptor) throws RemoteException {

        ChannelName channelName = channelDescriptor.getName();
        ServerChannel serverChannel = getChannel(channelName);

        if (serverChannel == null) {
            throw new IllegalStateException(
                "Could not create or join channel named "
                + channelName + " in Connection");
        }

        serverChannel.addClient(this.username, clientChannel);

    }

    /** {@inheritDoc} */
    public void leaveChannel(final ChannelName channelName)
            throws RemoteException {

        ServerChannel serverChannel = getChannel(channelName);
        serverChannel.removeClient(this.username);

    }

    private ServerChannel getChannel(final ChannelName channelName)
            throws RemoteException {

        ChannelManager channelManager = this.server.getChannelManager();
        ServerChannel serverChannel = channelManager.getChannel(
                this.community.getId(), channelName);
        return serverChannel;

    }

    /**
     * Retrieves all of the communities on the server.
     *
     * @return a collection containing every community
     * @throws RemoteException if an rmi error occurs
     */
    private Collection<Community> getAllCommunities() throws RemoteException {

        UserManager userManager = this.server.getUserManager();
        Collection<Community> communities = userManager.getAllCommunities();
        return communities;

    }

    /** {@inheritDoc} */
    public void add(final ChannelName channelName, final ChannelData data)
            throws RemoteException {

        ServerChannel channel = getChannel(channelName);
        channel.add(data);

    }

    /** {@inheritDoc} */
    public List<ChannelData> getLastData(final ChannelName channelName,
            final int count) throws RemoteException {

        ServerChannel channel = getChannel(channelName);
        return channel.getLastData(count);

    }

    public Collection<UserName> getActiveUsers(
            final ChannelName channelName) throws RemoteException {

        // Must be in the Connected (not logged in) state
        if (this.state != ConnectionState.CONNECTED) {
            System.err.println("[Connection] Attempt to get active users "
                    + "on connection in '"
                    + this.state + "' state");
            throw new IllegalStateException();
        }

        ChannelManager cm = server.getChannelManager();
        ServerChannel servChan = cm.getChannel(this.community.getId(),
                channelName);

        return servChan.getUsers();
    }

}
