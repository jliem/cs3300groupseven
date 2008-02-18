package colab.community;

import java.util.HashMap;
import java.util.Map;

public class CommunityManager {

	private final Map communities;
	
	public CommunityManager() {

		// Create an empty map of communities.
		communities = new HashMap<CommunityName, Community>();
		
	}
	
	public Community getCommunity(final CommunityName name) {
		return (Community) communities.get(name);
	}
	
}
