package colab.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;

import colab.client.remote.ClientChannelInterface;
import colab.common.channel.Channel;
import colab.common.channel.ChannelName;
import colab.server.remote.ServerChannelInterface;

public abstract class ServerChannel extends Channel
        implements ServerChannelInterface {

    private Collection<ClientChannelInterface> clients;

    public ServerChannel(final ChannelName name) throws RemoteException {

        super(name);

        // I suspect the line below is not necessary now that Channel
        // extends UnicastRemoteObject --J
        //UnicastRemoteObject.exportObject(this);

        this.clients = new ArrayList<ClientChannelInterface>();

    }

    public void addClient(final ClientChannelInterface client) {
        clients.add(client);
    }

}
