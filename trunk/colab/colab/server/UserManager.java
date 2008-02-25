package colab.server;

import java.rmi.RemoteException;
import java.util.Collection;

import colab.common.identity.IdentitySet;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import colab.common.remote.client.ColabClientInterface;

/**
 * A simple user manager that holds all users and communities in memory.
 */
final class UserManager {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /**
     * All of the communities that exist on a server.
     */
    private final IdentitySet<CommunityName, Community> communities;

    /**
     * All of the users that exist on a server.
     */
    private final IdentitySet<UserName, User> users;

    private ColabServer server;

    /**
     * Constructs an "empty" user manager with no communities or users.
     */
    public UserManager(final ColabServer server) {

        this.server = server;

        // Create an empty set of communities.
        communities = new IdentitySet<CommunityName, Community>();

        // Create an empty set of users.
        users = new IdentitySet<UserName, User>();

    }

    /**
     * Retrieves a community.
     *
     * @param name the name of the community
     * @return the community with the given name
     */
    public Community getCommunity(final CommunityName name) {
        return communities.get(name);
    }

    /**
     * Retrieves all of the communities on the server.
     *
     * @return a collection containing every community
     */
    public Collection<Community> getAllCommunities() {
        return communities;
    }

    /**
     * Adds a new community.
     *
     * @param community the new community to add
     */
    public void addCommunity(final Community community) {
        communities.add(community);
    }

    /**
     * Retrieves a user.
     *
     * @param name the name of the user to retrieve
     * @return the user with the given name
     */
    public User getUser(final UserName name) {
        return users.get(name);
    }

    /**
     * Adds a new user.
     *
     * @param user the new user to add
     */
    public void addUser(final User user) {
        users.add(user);
    }

    public void logIn(final CommunityName communityName, final UserName userName,
            final ColabClientInterface client) throws RemoteException {

        this.getCommunity(communityName).addClient(userName, client);
        ChannelManager channelManager = server.getChannelManager();
        Collection<ServerChannel> channels =
            channelManager.getChannels(communityName);
        for (ServerChannel channel : channels) {
            System.err.println("Sending shit to the client");
            client.channelAdded(channel.getChannelDescriptor());
        }

    }

}
