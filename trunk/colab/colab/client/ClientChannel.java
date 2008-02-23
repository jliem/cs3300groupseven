package colab.client;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import colab.common.channel.Channel;
import colab.common.channel.ChannelData;
import colab.common.channel.ChannelName;
import colab.common.channel.remote.ChannelInterface;
import colab.common.user.User;

/**
 * A client-side remote Channel object.
 */
public abstract class ClientChannel extends Channel
        implements ChannelInterface, Serializable {

    protected final Collection<User> members;

    public ClientChannel(final ChannelName name) throws RemoteException {

        super(name);

        // Create an empty collection of users
        members = new ArrayList<User>();

    }

    public abstract void add(ChannelData data);

    public abstract boolean remove(ChannelData data);

}
