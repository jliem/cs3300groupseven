package colab.community;

import java.rmi.Remote;
import java.util.Collection;

import colab.channel.ChannelManagerInterface;

/**
 * A remote class based in the server application.
 * Once a client has authenticated, the {@link ChannelManagerInterface}
 * is used to deal with communities.
 */
public interface CommunityManagerInterface extends Remote {

	public Community get(final CommunityName name);

	public Collection<Community> getAll();
	
}
