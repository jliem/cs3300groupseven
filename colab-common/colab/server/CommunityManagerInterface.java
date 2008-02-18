package colab.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import colab.community.Community;
import colab.community.CommunityName;

/**
 * A remote class based in the server application.
 * Once a client has authenticated, the {@link ChannelManagerInterface}
 * is used to deal with communities.
 */
public interface CommunityManagerInterface extends Remote {

	public Community get(final CommunityName name) throws RemoteException;

	public Collection<Community> getAll() throws RemoteException;
	
}
