package colab.server.user;

import java.util.Collection;

import colab.common.naming.CommunityName;

public interface CommunityStore {

    Collection<Community> getAll();

    void add(Community community);

    Community get(CommunityName name);

}
