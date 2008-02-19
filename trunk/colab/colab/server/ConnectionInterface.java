package colab.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import colab.community.CommunityName;
import colab.user.UserName;

public interface ConnectionInterface extends Remote {

	public boolean logIn(UserName username, String password)
		throws RemoteException;

	public boolean logIn(CommunityName communityName, String password)
		throws RemoteException;
	
}
