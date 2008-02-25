package colab.server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import colab.common.channel.ChannelDescriptor;
import colab.common.identity.Identifiable;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import colab.common.remote.client.ColabClientInterface;

/**
 * Represents a community which can be joined by users.
 */
class Community implements Identifiable<CommunityName>, Serializable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * A unique string identifying this community.
     */
    private final CommunityName name;

    /**
     * The users which have joined this community and can log in to it.
     */
    private final Collection<User> members;

    /**
     * The password to join this community.
     */
    private Password password;

    /**
     * A list of actively connected clients
     */
    private Map<UserName, ColabClientInterface> clients;

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
        members = new ArrayList<User>();

    }

    /**
     * Constructs a new Community.
     *
     * @param name a unique string identifying this community
     * @param password the password to join this community
     */
    public Community(final String name, final String password) {
        this(new CommunityName(name), new Password(password));
    }

    /**
     * Returns the string which identifies this community.
     *
     * @return the name of this community
     */
    public final CommunityName getId() {
        return name;
    }

    /**
     * Returns the users which are members of this community.
     *
     * @return a collection containing every user of this community
     */
    public final Collection<User> getMembers() {
        return members;
    }

    /**
     * Tells all clients that a new channel has been added.
     *
     * @param channelDescriptor descriptor of the added channel
     * @throws RemoteException if an rmi error occurs
     */
    public void channelAdded(final ChannelDescriptor channelDescriptor) throws RemoteException {
        for (final UserName userName : this.clients.keySet()) {
            this.clients.get(userName).channelAdded(channelDescriptor);
        }
    }

    public final void addClient(final UserName username,
            final ColabClientInterface client) {
        clients.put(username, client);
    }

    public void removeClient(final UserName username) {
        clients.remove(username);
    }


    /**
     * Verifies whether a given password string is correct for this community.
     *
     * @param attempt an input string which may be a correct password
     * @return true if the given password is correct, false otherwise
     */
    public final boolean checkPassword(final String attempt) {
        return password.checkPassword(attempt);
    }

    public final boolean isMember(final User user) {
        return members.contains(user);
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
     * @param user the user attempting to log in to the community
     * @param passAttempt the password that a new user may be using to
     *                    join the community (may be null)
     * @return true if the authentication succeeds, false if it fails
     */
    public final boolean authenticate(final User user,
            final String passAttempt) {

        // If a correct password was provided, the user can join.
        if (passAttempt != null && checkPassword(passAttempt)) {
            members.add(user);
            return true;
        }

        // If neither, the user cannot authenticate to this community.
        return false;

    }

}
