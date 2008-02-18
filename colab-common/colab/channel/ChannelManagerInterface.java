package colab.channel;

import java.rmi.Remote;

/**
 * ChannelManager is a remote class based in the server application.
 * Once a client has authenticated and joined a community, a
 * ChannelManager is created for that session, to provide Channel
 * objects when the user joins a channel.  
 */
public interface ChannelManagerInterface extends Remote {

	public Channel getChannel(String channelName);
	
}
