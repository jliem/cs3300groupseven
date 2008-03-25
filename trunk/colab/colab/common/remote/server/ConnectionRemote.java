package colab.common.remote.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDataIdentifier;
import colab.common.channel.ChannelDescriptor;
import colab.common.exception.CommunityDoesNotExistException;
import colab.common.naming.ChannelName;
import colab.common.naming.CommunityName;
import colab.common.naming.UserName;
import colab.common.remote.client.ChannelRemote;
import colab.server.user.Community;
import colab.server.user.Password;

/**
 * A remote object on the server which represents a client's session.
 */
public interface ConnectionRemote extends Remote {

    /**
     * Attempts to authenticate a user, using the name and password.
     *
     * Throws IllegalStateException is the connection is in the wrong state.
     *
     * @param username the name of the user attempting to log in
     * @param password the password that the client is attempting to use;
     *                 may be null, but authentication will fail
     * @throws RemoteException if an rmi error occurs
     */
    public void logIn(UserName username, char[] password)
        throws RemoteException;

    /**
     * Logs out the user.
     *
     * @throws RemoteException if an rmi error occurs
     */
    public void logOutUser() throws RemoteException;

    /**
     * Attempts to log into a community, using the name and optional password.
     *
     * Throws IllegalStateException is the connection is in the wrong state.
     *
     * @param communityName the name of the community to log in to
     * @param password the password that the client is attempting to use;
     *                 ignored if the user is already a member of the
     *                 community, may be null
     * @throws RemoteException if an rmi error occurs
     */
    public void logIn(CommunityName communityName, char[] password)
        throws RemoteException;

    /**
     * Logs out of the community.
     *
     * @throws RemoteException if an rmi error occurs
     */
    public void logOutCommunity() throws RemoteException;

    /**
     * Retrieves the names of every community on the server.
     *
     * @return a collection containing every community name
     * @throws RemoteException if an rmi error occurs
     */
    public Collection<CommunityName> getAllCommunityNames()
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
    public Collection<CommunityName> getMyCommunityNames()
        throws RemoteException;

    /**
     * Retrieves a channel.
     * The channel will be created if it does not exist.
     *
     * @param clientChannel a remote object representing the
     *                      channel on the client side
     * @param channelDescriptor the dsecriptor of the channel being joined
     * @throws RemoteException if an rmi error occurs
     */
    public void joinChannel(ChannelRemote clientChannel,
            ChannelDescriptor channelDescriptor) throws RemoteException;

    /**
     * Indicates that the client has exited from a channel
     * and should not continue to receive updates from it.
     *
     * @param channelName the name of the channel being left
     * @throws RemoteException if an rmi error occurs
     */
    public void leaveChannel(ChannelName channelName) throws RemoteException;

    /**
     * Checks whether the logged in user is a member of a given community.
     *
     * @param communityName the community name
     * @return true if the user is a member of the community, false otherwise
     * @throws CommunityDoesNotExistException if the community did not exist
     * @throws RemoteException if an rmi error occurs
     */
    public boolean isMember(CommunityName communityName)
        throws RemoteException, CommunityDoesNotExistException;

    /**
     * Indicates that a client is becoming a new member
     * of a community.
     *
     * @param communityName the name of the community being joined
     * @throws RemoteException if an rmi error occurs
     * @throws CommunityDoesNotExistException if the community does not exist
     */
    public void addAsMember(CommunityName communityName)
        throws RemoteException, CommunityDoesNotExistException;

    /**
     * Indicates that the client is longer a member of
     * this community.
     *
     * @param communityName the name of the community being left
     * @throws RemoteException if an rmi error occurs
     */
    public void removeAsMember(CommunityName communityName)
        throws RemoteException, CommunityDoesNotExistException;


    /**
     * Gets the last *count* pieces of data from the specified Channel.
     *
     * @param channelName the channel to retrieve data from
     * @param count the number of data objects to retrieve
     * @return a list of data objects
     * @throws RemoteException if an rmi error occurs
     */
    public List<ChannelData> getLastData(ChannelName channelName, int count)
            throws RemoteException;

    /**
     * Adds this channel and data to the connection.
     *
     * @param channelName the channel to add
     * @param data the channel's data
     * @return the id assigned to the data that was added
     * @throws RemoteException if an rmi error occurs
     */
    public ChannelDataIdentifier add(ChannelName channelName, ChannelData data)
            throws RemoteException;

    /**
     * Get a list of all users currently joined to this channel.
     * @param channelName the channel to look up
     * @return a list of all the users in this channel
     * @throws RemoteException if a remote exception occurs
     */
    public Collection<UserName> getActiveUsers(ChannelName channelName)
            throws RemoteException;

    /**
     * Returns the user that is logged in.
     *
     * Throws IllegalStateException if no user is logged in.
     *
     * @return a user that has authenticated on this connection
     */
    public UserName getUserName() throws RemoteException;

    /**
     * Creates a new user in this connection.
     * @param userName the user name
     * @param password the password to use
     * @throws RemoteException if an rmi error occurs
     */
    public void createUser(String userName, char[] password)
            throws RemoteException;

    /**
     * Creates a new community in this connection.
     *
     * @param name the community name
     * @param password the password used to join the community
     * @throws RemoteException if an rmi error occurs
     */
    public void createCommunity(String name, char[] password)
        throws RemoteException;

    /**
     * Creates a new community in this connection.
     *
     * @param name the community name
     * @param password the password used to join the community
     * @throws RemoteException if an rmi error occurs
     */

    public void createCommunity(CommunityName name, Password password)
        throws RemoteException;

    /**
     * Creates a new channel in the currently logged-in community.
     *
     * @param channelDesc descriptor of the channel
     */
    public void createChannel(ChannelDescriptor channelDesc)
        throws RemoteException ;

    /**
     * Checks whether a user has logged into the program.
     *
     * @return true if the user has logged in
     */
    public boolean hasUserLogin()
        throws RemoteException;
    
    /**
     * Checks whether a given password is correct for a given community
     * @param commName 
     * @param password
     * @return
     * @throws RemoteException
     */
    public boolean checkCommunityPassword(CommunityName commName, char[] password)
    	throws RemoteException;

    
    public void addUserToCommunity(UserName user, CommunityName comm)
    	throws RemoteException;
}
