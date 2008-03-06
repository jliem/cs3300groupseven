package colab.server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import colab.common.channel.ChannelDescriptor;
import colab.common.identity.Identifiable;
import colab.common.identity.IdentitySet;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import colab.common.remote.client.ColabClientInterface;
import colab.server.connection.Connection;
import colab.server.connection.ConnectionIdentifier;
import colab.server.event.DisconnectEvent;
import colab.server.event.DisconnectListener;

/**
 * Represents a community which can be joined by users.
 */
public final class Community implements Identifiable<CommunityName>,
        DisconnectListener, Serializable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * A unique string identifying this community.
     */
    private final CommunityName name;

    /**
     * The users which have joined this community and can log in to it.
     */
    private final Collection<UserName> members;

    /**
     * The password to join this community.
     */
    private Password password;

    /**
     * A list of actively connected clients.
     */
    private final IdentitySet<ConnectionIdentifier, Connection> clients;

    /**
     * Constructs a new community with the given name and password.
     *
     * @param name the name identifying the community
     * @param password the password required to join the community
     */
    public Community(final CommunityName name, final Password password) {

        // Set the community name
        this.name = name;

        // Set the community password
        this.password = password;

        // Create an empty collection of users
        this.members = new ArrayList<UserName>();

        this.clients = new IdentitySet<ConnectionIdentifier, Connection>();

    }

    /**
     * Constructs a new Community.
     *
     * @param name a unique string identifying this community
     * @param password the password to join this community
     */
    public Community(final String name, final String password) {
        this(new CommunityName(name), new Password(password.toCharArray()));
    }

    /**
     * Returns the string which identifies this community.
     *
     * @return the name of this community
     */
    public CommunityName getId() {
        return name;
    }

    /**
     * Returns the users which are members of this community.
     *
     * @return a collection containing every user of this community
     */
    public Collection<UserName> getMembers() {
        return members;
    }

    /**
     * Tells all clients that a new channel has been added.
     *
     * @param channelDescriptor descriptor of the added channel
     */
    public void channelAdded(final ChannelDescriptor channelDescriptor) {

        for (final Connection connection : this.clients) {
            ColabClientInterface client = connection.getClient();
            try {
                client.channelAdded(channelDescriptor);
            } catch (final RemoteException re) {
                connection.disconnect(re);
            }
        }

    }

    public void addClient(final Connection connection) {
        connection.addDisconnectListener(this);
        this.clients.add(connection);
    }

    public void removeClient(final Connection connection) {
        this.clients.remove(connection.getId());
        connection.removeDisconnectListener(this);
    }

    public Password getPassword() {
        return this.password;
    }

    /**
     * Verifies whether a given password string is correct for this community.
     *
     * @param attempt an input string which may be a correct password
     * @return true if the given password is correct, false otherwise
     */
    public boolean checkPassword(final char[] attempt) {
        return password.checkPassword(attempt);
    }

    public boolean isMember(final UserName username) {
        return members.contains(username);
    }

    public boolean isActive(final UserName userName) {
        for (Connection connection : this.clients) {
            if (connection.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles a user attempt to log in to this community.
     *
     * If the user is already a member, the authentication is approved.
     *
     * If the user is not a member, but provides a correct password, the
     * user becomes a member of the community, and authentication is approved.
     *
     * If neither, authentication is denied.
     *
     * @param username the user attempting to log in to the community
     * @param passAttempt the password that a new user may be using to
     *                    join the community (may be null)
     * @return true if the authentication succeeds, false if it fails
     */
    public boolean authenticate(final UserName username,
            final char[] passAttempt) {

        // If a correct password was provided, the user can join.
        if (passAttempt != null && checkPassword(passAttempt)) {
            members.add(username);
            return true;
        }

        // If neither, the user cannot authenticate to this community.
        return false;

    }

    /** {@inheritDoc} */
    public void handleDisconnect(final DisconnectEvent event) {
        ConnectionIdentifier connectionId = event.getConnectionId();
        this.clients.removeId(connectionId);
    }

}
