package colab.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import colab.community.Community;
import colab.community.CommunityName;
import colab.user.User;
import colab.user.UserName;

/**
 * A remote class based in the server application.
 * Once a client has authenticated, the {@link UserManagerInterface}
 * is used to deal with communities and users.
 */
public interface UserManagerInterface extends Remote {

    public Community getCommunity(final CommunityName name) throws RemoteException;

    public Collection<Community> getAllCommunities() throws RemoteException;
    
    public User getUser(UserName name) throws RemoteException;
    
}
