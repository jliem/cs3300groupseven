package colab.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import colab.common.channel.Channel;
import colab.common.channel.ChannelName;
import colab.common.channel.remote.ChannelInterface;

public abstract class ServerChannel extends Channel
        implements ChannelInterface {

    private Collection<ChannelInterface> clients;

    public ServerChannel(final ChannelName name) throws RemoteException {

        super(name);

        this.clients = new ArrayList<ChannelInterface>();

    }

    public void addClient(final ChannelInterface client) {
        clients.add(client);
    }

}
