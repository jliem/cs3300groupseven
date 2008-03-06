package colab.server.store;

import java.util.Collection;

import colab.common.naming.CommunityName;
import colab.server.Community;

public interface CommunityStore {

    Collection<Community> getAll();

    void add(Community community);

    Community get(CommunityName name);

}
