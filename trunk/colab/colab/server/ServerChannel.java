package colab.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


import colab.common.channel.Channel;
import colab.common.channel.ChannelName;
import colab.server.remote.ServerChannelInterface;

public class ServerChannel extends Channel
        implements ServerChannelInterface {

    public ServerChannel(final ChannelName name) throws RemoteException {
        super(name);
        UnicastRemoteObject.exportObject(this);
    }

}
