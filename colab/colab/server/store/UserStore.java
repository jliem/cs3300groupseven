package colab.server.store;

import colab.common.naming.UserName;
import colab.server.User;

public interface UserStore {

    void add(User user);

    User get(UserName name);

}
