package colab.common;

/**
 * An enumeration of possible login statuses for a connection.
 */
public enum ConnectionState {

    /**
     * Client has no connection to the server.
     */
    DISCONNECTED("disconnected", false, false, false),

    /**
     * Client has a connection to the server,
     * but no authentication has taken place.
     */
    CONNECTED("connected", true, false, false),

    /**
     * User has been authenticated, but no
     * community has been joined.
     */
    LOGGED_IN("logged in", true, true, false),

    /**
     * The user is logged into a community and
     * can actively use the system.
     */
    ACTIVE("active", true, true, true);

    /**
     * A string representation of this state.
     */
    private final String str;

    /**
     * Indicates whether a connection has been made.
     */
    private final boolean connected;

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
     * @param str a string represetation of the state
     * @param connection whether a connection has been made
     * @param userLogin whether a user has logged in
     * @param communityLogin whether a user has logged in to a community
     */
    private ConnectionState(final String str, final boolean connection,
            final boolean userLogin, final boolean communityLogin) {
        this.str = str;
        this.connected = connection;
        this.userLogin = userLogin;
        this.communityLogin = communityLogin;
    }

    /**
     * Determines whether a connection has been made.
     *
     * @return true if connected, false otherwise.
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Determines whether a user is logged in on this connection.
     *
     * @return true is a user is logged in, false otherwise
     */
    public boolean hasUserLogin() {
        return userLogin;
    }

    /**
     * Determines whether a user has logged in to a community on this
     * connection.
     *
     * @return true if the user has logged into a community, false otherwise
     */
    public boolean hasCommunityLogin() {
        return communityLogin;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return str;
    }

}
