package colab.client;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

import colab.client.remote.ChannelInterface;
import colab.common.channel.Channel;
import colab.common.channel.ChannelName;
import colab.common.user.UserName;

/**
 * A client-side remote Channel object.
 */
abstract class ClientChannel extends Channel implements ChannelInterface {

    protected final Set<UserName> members;

    public ClientChannel(final ChannelName name) throws RemoteException {

        super(name);

        // Create an empty collection of users
        members = new HashSet<UserName>();

    }


    public void userJoined(UserName userName) throws RemoteException {
        members.add(userName);
    }

    public void userLeft(UserName userName) throws RemoteException {
        boolean result = members.remove(userName);

        // Check that remove was successful
        if (result == false) throw new IllegalStateException("Could not remove user " +
                userName.toString() + " from members list in ClientChannel");
    }
}
