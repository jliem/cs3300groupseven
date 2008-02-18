package colab.channel;

import java.rmi.Remote;

/**
 * A remote class based in the server application.
 * Once a client has authenticated and joined a community, a
 * {@link ChannelManagerInterface} is created for that session,
 * to provide {@link Channel} objects when the user joins a channel.  
 */
public interface ChannelManagerInterface extends Remote {

	public Channel getChannel(String channelName);
	
}
