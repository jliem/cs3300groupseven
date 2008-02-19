package colab.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ColabServerInterface extends Remote {

	public ConnectionInterface connect() throws RemoteException;

	public UserManagerInterface getUserManager() throws RemoteException;
	
}
