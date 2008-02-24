package colab.client;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

import colab.common.channel.Channel;
import colab.common.channel.ChannelName;
import colab.common.user.UserName;

/**
 * A client-side remote Channel object.
 */
public abstract class ClientChannel extends Channel {

    protected final Set<UserName> members;

    public ClientChannel(final ChannelName name) throws RemoteException {

        super(name);

        // Create an empty collection of users
        members = new HashSet<UserName>();

    }

}
