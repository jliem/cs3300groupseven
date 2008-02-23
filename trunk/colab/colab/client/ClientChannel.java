package colab.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;

import colab.client.remote.ClientChannelInterface;
import colab.common.channel.Channel;
import colab.common.channel.ChannelData;
import colab.common.channel.ChannelName;
import colab.common.user.User;

/**
 * A client-side remote Channel object.
 */
public abstract class ClientChannel extends Channel
        implements ClientChannelInterface {

    protected final Collection<User> members;

    public ClientChannel(final ChannelName name) throws RemoteException {

        super(name);

        UnicastRemoteObject.exportObject(this);

        // Create an empty collection of users
        members = new ArrayList<User>();

    }

    public abstract void add(ChannelData data);

    public abstract boolean remove(ChannelData data);

}
