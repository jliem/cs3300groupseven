package colab.server.user;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import colab.common.DebugManager;
import colab.common.channel.ChannelDescriptor;
import colab.common.identity.Identifiable;
import colab.common.identity.IdentitySet;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import colab.common.remote.client.ColabClientRemote;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;
import colab.common.xml.XmlSerializable;
import colab.server.connection.Connection;
import colab.server.connection.ConnectionIdentifier;
import colab.server.event.CommunityEvent;
import colab.server.event.CommunityListener;
import colab.server.event.DisconnectEvent;
import colab.server.event.DisconnectListener;

/**
 * Represents a community which can be joined by users.
 *
 * This class represents the community domain object, and also
 * provides server-related services by keeping track of which
 * clients are connected and providing them with community-wide
 * notifications (such as informing clients of newly-added channels).
 */
public final class Community implements Identifiable<CommunityName>,
        DisconnectListener, Serializable, XmlSerializable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * A unique string identifying this community.
     */
    private CommunityName name;

    /**
     * The password to join this community.
     */
    private Password password;

    /**
     * The users which have joined this community and can log in to it.
     */
    private final Set<UserName> members = new HashSet<UserName>();

    /**
     * List of moderators (each moderator should also be in the
     * members Set).
     */
    private final Set<UserName> moderators = new HashSet<UserName>();

    /**
     * A list of actively connected clients.
     */
    private final IdentitySet<ConnectionIdentifier, Connection> clients =
        new IdentitySet<ConnectionIdentifier, Connection>();

    private Set<CommunityListener> listeners = new HashSet<CommunityListener>();

    public void addListener(final CommunityListener listener) {
        listeners.add(listener);
    }

    public void removeListener(final CommunityListener listener) {
        listeners.remove(listener);
    }

    private void fireEvent(final CommunityEvent event) {
        for (CommunityListener listener : listeners) {
            listener.handleEvent(event);
        }
    }

    /**
     * Constructs a new, empty community.
     */
    public Community() {
    }

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
     * Returns the password object.
     *
     * @return the password
     */
    public Password getPassword() {
        return password;
    }

    /**
     * Adds a user to this community's member list.
     *
     * @param username the name of the user to add
     */
    public void addMember(final UserName username) {
        members.add(username);
        fireEvent(new CommunityEvent());
    }

    /**
     * Adds a user as a moderator. The user must already
     * be a member of the community.
     *
     * @param username the name of the user to add
     * @throws IllegalStateException if the user is not a member
     */
    public void addAsModerator(final UserName username) {
        // Check whether this user is a member
        if (!members.contains(username)) {
            throw new IllegalStateException("Could not add " + username
                    + " as a moderator because they are not listed as "
                    + "a member!");
        }

        moderators.add(username);
    }

    /**
     * Removes a member from this community's member list.
     *
     * @param username the name of the user to remove
     */
    public boolean removeMember(final UserName username) {

        boolean result = members.remove(username);

        // If they were a moderator, remove them from that list
        moderators.remove(username);

        fireEvent(new CommunityEvent());

        return result;
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
            ColabClientRemote client = connection.getClient();
            try {
                client.channelAdded(channelDescriptor);
            } catch (final RemoteException re) {
                DebugManager.remote(re);
                connection.disconnect(re);
            }
        }

    }

    /**
     * Informs the community that a client has logged in.
     *
     * @param connection the connection object to add
     */
    public void addClient(final Connection connection) {
        connection.addDisconnectListener(this);
        this.clients.add(connection);
    }

    /**
     * Informs the community that a user has logged out or disconnected.
     *
     * @param connection the connect object to remove
     */
    public void removeClient(final Connection connection) {
        this.clients.remove(connection);
        connection.removeDisconnectListener(this);
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

    /**
     * Determines whether a user is a member of the community.
     *
     * @param username the username to check
     * @return true if the user is a community member, false otherwise
     */
    public boolean isMember(final UserName username) {
        return members.contains(username);
    }

    /**
     * Determines whether a user is a moderator of the community.
     *
     * @param username the username to check
     * @return true if the user is a community moderator, false otherwise
     */
    public boolean isModerator(final UserName username) {
        return moderators.contains(username);
    }

    /**
     * Determines whether a user is currently logged into the community.
     *
     * @param userName the username to check
     * @return true if the user is currently logged in, false otherwise
     */
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
            addMember(username);
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

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Community";
    }

    /** {@inheritDoc} */
    public XmlNode toXml() {

        XmlNode node = new XmlNode(xmlNodeName());
        node.setAttribute("name", name.getValue());
        node.setAttribute("password", password.getHash());

        for (final UserName username : members) {
            node.addChild(username.toXml());
        }

        return node;

    }

    /** {@inheritDoc} */
    public void fromXml(final XmlNode node) throws XmlParseException {

        this.password = new Password(node.getAttribute("password"));
        this.name = new CommunityName(node.getAttribute("name"));

        for (final XmlNode child : node.getChildren()) {
            UserName username = new UserName();
            username.fromXml(child);
            addMember(username);
        }

    }

}
