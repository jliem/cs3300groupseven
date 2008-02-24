package colab.server.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import colab.client.remote.ChannelInterface;
import colab.common.channel.ChannelData;
import colab.common.channel.ChannelName;
import colab.common.community.CommunityName;
import colab.common.user.UserName;

/**
 * A remote object on the server which represents a client's session.
 */
public interface ConnectionInterface extends Remote {

    /**
     * Attempts to authenticate a user, using the name and password.
     *
     * Throws IllegalStateException is the connection is in the wrong state.
     *
     * @param username the name of the user attempting to log in
     * @param password the password that the client is attempting to use;
     *                 may be null, but authentication will fail
     * @return true if the login was performed successfully, false otherwise
     * @throws RemoteException if an rmi error occurs
     */
    boolean logIn(UserName username, char[] password)
        throws RemoteException;

    /**
     * Attempts to log into a community, using the name and optional password.
     *
     * Throws IllegalStateException is the connection is in the wrong state.
     *
     * @param communityName the name of the community to log in to
     * @param password the password that the client is attempting to use;
     *                 ignored if the user is already a member of the
     *                 community, may be null
     * @return true if the login was performed successfully, false otherwise
     * @throws RemoteException if an rmi error occurs
     */
    boolean logIn(CommunityName communityName, String password)
        throws RemoteException;

    /**
     * Retrieves the names of every community on the server.
     *
     * @return a collection containing every community name
     * @throws RemoteException if an rmi error occurs
     */
    Collection<CommunityName> getAllCommunityNames()
        throws RemoteException;

    /**
     * Retrieves the names of every community of which the currently
     * logged-in user is a member.
     *
     * Throws IllegalStateException if no user is logged in.
     *
     * @return a collection containing the name of every community
     *         in which the user has membership
     * @throws RemoteException if an rmi error occurs
     */
    Collection<CommunityName> getMyCommunityNames()
        throws RemoteException;

    /**
     * Retrieves a channel.
     * The channel will be created if it does not exist.
     *
     * @param clientChannel a remote object representing the
     *                      channel on the client side
     * @param channelName the name of the channel being joined
     * @return a remote reference to the requested channel
     * @throws RemoteException if an rmi error occurs
     */
    void joinChannel(ChannelInterface clientChannel,
            ChannelName channelName) throws RemoteException;

    /**
     * Indicates that the client has exited from a channel
     * and should not continue to receive updates from it.
     *
     * @param channelName the name of the channel being left
     * @throws RemoteException if an rmi error occurs
     */
    void leaveChannel(ChannelName channelName) throws RemoteException;

    List<ChannelData> getLastData(ChannelName channelName, int count)
            throws RemoteException;

    void add(ChannelName channelName, ChannelData data)
            throws RemoteException;

}
