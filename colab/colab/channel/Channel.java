package colab.channel;

import java.util.ArrayList;
import java.util.Collection;

import colab.identity.Identifiable;
import colab.user.User;

/**
 * Represents a channel in the system, including data, usernames and metadata.
 */
public abstract class Channel implements Identifiable<ChannelName> {

	protected final Collection<User> members;
	protected final ChannelName name;

	public Channel(final ChannelName name) {
		
		// Set the channel name
		this.name = name;
		
		// Create an empty collection of users
		members = new ArrayList<User>();
		
	}

	public abstract void add(ChannelData data);

	public abstract boolean remove(ChannelData data);

	public ChannelName getId() {
		return name;
	}

}
