package colab.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;

import colab.client.ClientChannel;
import colab.common.community.Community;
import colab.common.community.CommunityName;
import colab.common.user.User;
import colab.common.user.UserName;
import colab.server.remote.ConnectionInterface;
import colab.server.remote.ServerChannelInterface;

/**
 * Server implementation of {@link ConnectionInterface}.
 */
public class Connection extends UnicastRemoteObject
        implements ConnectionInterface {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * An enumeration of possible login statuses for a connection.
     */
    public enum STATE {

        /**
         * Client has a connection to the server,
         * but no authentication has taken place.
         */
        CONNECTED(false, false),

        /**
         * User has been authenticated, but no
         * community has been joined.
         */
        LOGGED_IN(true, false),

        /**
         * The user is logged into a community and
         * can actively use the system.
         */
        ACTIVE(true, true);

        /**
         * Indicates whether this is a state in which
         * a user is logged in.
         */
        private final boolean userLogin;

        /**
         * Indicates whether this is a state in which
         * the a user has logged in to a community.
         */
        private final boolean communityLogin;

        /**
         * Constructs a state.
         *
         * @param userLogin whether a user has logged in
         * @param communityLogin whether a user has logged in to a community
         */
        private STATE(final boolean userLogin, final boolean communityLogin) {
            this.userLogin = userLogin;
            this.communityLogin = communityLogin;
        }

    }

    /**
     * The server which created this connection.
     */
    private final ColabServer server;

    /**
     * The current state of the connection.
     */
    private STATE state;

    /**
     * The user that has logged in on this connection (if any).
     */
    private User user;

    /**
     * The community that has been logged into on this connection (if any).
     */
    private Community community;

    /**
     * Constructs a new Connection.
     *
     * @param server the server to which the client is connected
     * @throws RemoteException if an rmi error occurs
     */
    public Connection(final ColabServer server) throws RemoteException {

        // Keep a reference to the server
        this.server = server;

        // Put the connection into the initial state
        this.state = STATE.CONNECTED;

    }

    /**
     * Determines whether a user is logged in on this connection.
     *
     * @return true is a user is logged in, false otherwise
     */
    public final boolean hasUserLogin() {
        return this.state.userLogin;
    }

    /**
     * Determines whether a user has logged in to a community on this
     * connection.
     *
     * @return true if the user has logged into a community, false otherwise
     */
    public final boolean hasCommunityLogin() {
        return this.state.communityLogin;
    }

    /**
     * Returns the state of this connection.
     *
     * @return the state object representing the connection's login status
     */
    public final STATE getState() {
        return this.state;
    }

    /**
     * Returns the user that is logged in.
     *
     * Throws IllegalStateException if no user is logged in.
     *
     * @return a user that has authenticated on this connection
     */
    public final User getUser() {

        if (!hasUserLogin()) {
            throw new IllegalStateException("Not logged in as user");
        }

        return this.user;

    }

    /**
     * Returns the community that is active on this connection.
     * The connection must be logged in to a community.
     *
     * @return a community to which the user has joined in this session
     */
    public final Community getCommunity() {

        if (!hasCommunityLogin()) {
            throw new IllegalStateException("Not logged in to a community");
        }

        return this.community;

    }

    /** {@inheritDoc} */
    public final boolean logIn(final UserName username, final String password)
            throws RemoteException {

        // Must be in the Connected (not logged in) state
        if (this.state != STATE.CONNECTED) {
            throw new IllegalStateException();
        }

        // Check the validity of login credentials
        UserManager userManager = server.getUserManager();
        User userAttempt = userManager.getUser(username);
        boolean correct =
            userAttempt != null // user exists
            && password != null // password was provided
            && userAttempt.checkPassword(password); // password is correct

        // Advance to the next state if correct
        if (correct) {
            this.user = userAttempt;
            this.state = STATE.LOGGED_IN;
        }

        return correct;

    }

    /** {@inheritDoc} */
    public final boolean logIn(final CommunityName communityName,
            final String password) throws RemoteException {

        // Must be in the Logged In (not yet in a community) state
        if (this.state != STATE.LOGGED_IN) {
            throw new IllegalStateException();
        }

        // Check the validity of login credentials
        UserManager userManager = server.getUserManager();
        Community communityAttempt = userManager.getCommunity(communityName);
        boolean correct =
            communityAttempt != null // community exists
            && communityAttempt.authenticate(this.user, password);

        // Advance to the next state if correct
        if (correct) {
            this.community = communityAttempt;
            this.state = STATE.ACTIVE;
        }

        return correct;

    }

    /** {@inheritDoc} */
    public final Collection<CommunityName> getAllCommunityNames()
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
    public final Collection<CommunityName> getMyCommunityNames()
            throws RemoteException {

        if (!hasUserLogin()) {
            throw new IllegalStateException("Not logged in as user");
        }

        Collection<Community> communities = getAllCommunities();

        Collection<CommunityName> communityNames =
            new ArrayList<CommunityName>(communities.size());

        for (Community c : communities) {
            if (c.getMembers().contains(this.user)) {
                communityNames.add(c.getId());
            }
        }

        return communityNames;

    }

    /** {@inheritDoc} */
    public final ServerChannelInterface joinChannel(
            final ClientChannel clientChannel) throws RemoteException {

        ChannelManager channelManager = this.server.getChannelManager();
        ServerChannel serverChannel = channelManager.getChannel(
                this.community.getId(), clientChannel.getId());
        serverChannel.addClient(clientChannel);
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

}
