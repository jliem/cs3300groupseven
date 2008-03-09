package colab.server;

import colab.common.naming.UserName;

public interface UserStore {

    void add(User user);

    User get(UserName name);

}
