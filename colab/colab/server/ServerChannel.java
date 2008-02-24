package colab.server;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import colab.client.remote.ChannelInterface;
import colab.common.channel.Channel;
import colab.common.channel.ChannelData;
import colab.common.channel.ChannelName;
import colab.common.user.UserName;

public abstract class ServerChannel extends Channel
        implements ChannelInterface {

    private Map<UserName, ChannelInterface> clients;

    public ServerChannel(final ChannelName name) throws RemoteException {

        super(name);

        this.clients = new HashMap<UserName, ChannelInterface>();

    }

    public final void addClient(final UserName username,
            final ChannelInterface client) {
        clients.put(username, client);
    }

    public void removeClient(final UserName username) {
        clients.remove(username);
    }

    /**
     *
     * @return a list of users in this Channel
     */
    public Collection<UserName> getUsers() {
        return new HashSet<UserName>(clients.keySet());
    }

    public abstract List<ChannelData> getLastData(int count);

    protected void sendToAll(final ChannelData data) throws RemoteException {
        for (final UserName userName : this.clients.keySet()) {
            if (!userName.equals(data.getCreator())) {
                this.clients.get(userName).add(data);
            }
        }
    }

}
