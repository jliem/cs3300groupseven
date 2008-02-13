package colab.common;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a channel in the system, including data, usernames and metadata.
 * 
 * @author JL
 *
 */
public abstract class Channel {
	
	protected ArrayList<User> userList;
	
	public Channel() {
		userList = new ArrayList<User>();
	}
	
	public abstract void add(ChannelData data);
	public abstract boolean remove(ChannelData data);
}
