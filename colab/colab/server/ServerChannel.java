package colab.server;

import java.rmi.RemoteException;
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

        this.clients = new ArrayList<ClientChannelInterface>();

    }

    public void addClient(final ClientChannelInterface client) {
        clients.add(client);
    }

}
