package colab.community;

import java.rmi.Remote;

public interface CommunityManagerInterface extends Remote {

	public Community get(final CommunityName name);

}
