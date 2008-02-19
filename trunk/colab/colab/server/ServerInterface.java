package colab.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

	public ConnectionInterface connect() throws RemoteException;

	public UserManagerInterface getUserManager() throws RemoteException;
	
}
