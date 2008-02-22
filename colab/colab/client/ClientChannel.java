package colab.client;

import java.util.ArrayList;
import java.util.Collection;


import colab.common.channel.Channel;
import colab.common.channel.ChannelData;
import colab.common.channel.ChannelName;
import colab.common.user.User;

/**
 * Represents a channel in the system, including data, usernames and metadata.
 */
public abstract class ClientChannel extends Channel {

    protected final Collection<User> members;

    public ClientChannel(final ChannelName name) {

        super(name);

        // Create an empty collection of users
        members = new ArrayList<User>();

    }

    public abstract void add(ChannelData data);

    public abstract boolean remove(ChannelData data);

}
