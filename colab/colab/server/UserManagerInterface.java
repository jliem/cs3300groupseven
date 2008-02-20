package colab.server;

import java.rmi.Remote;

/**
 * A remote class based in the server application.
 * Once a client has authenticated, the {@link UserManagerInterface}
 * is used to deal with communities and users.
 *
 * TODO: I'm not sure if we need a remote interface for this anymore -cmartin
 */
public interface UserManagerInterface extends Remote {

//    public Community getCommunity(final CommunityName name)
//        throws RemoteException;

//    public Collection<Community> getAllCommunities()
//        throws RemoteException;

//    public User getUser(UserName name)
//        throws RemoteException;

}
