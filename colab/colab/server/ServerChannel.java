package colab.server;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import colab.common.channel.Channel;
import colab.common.channel.ChannelName;
import colab.common.channel.remote.ChannelInterface;
import colab.common.user.UserName;

public abstract class ServerChannel extends Channel
        implements ChannelInterface {

    private Map<UserName, ChannelInterface> clients;

    public ServerChannel(final ChannelName name) throws RemoteException {

        super(name);

        this.clients = new HashMap<UserName, ChannelInterface>();

    }

    public void addClient(final UserName username,
            final ChannelInterface client) {
        clients.put(username, client);
    }

    public void removeClient(final UserName username) {
        clients.remove(username);
    }

}
