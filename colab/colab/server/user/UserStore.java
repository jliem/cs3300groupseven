package colab.server.user;

import colab.common.naming.UserName;

public interface UserStore {

    void add(User user);

    User get(UserName name);

}
