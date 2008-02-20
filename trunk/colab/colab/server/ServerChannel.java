package colab.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import colab.server.remote.ChannelInterface;

public class ServerChannel extends UnicastRemoteObject
        implements ChannelInterface {

    public ServerChannel() throws RemoteException {
    }

}
