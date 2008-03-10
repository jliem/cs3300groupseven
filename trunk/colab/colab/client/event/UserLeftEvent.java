package colab.client.event;

import colab.common.naming.UserName;

public class UserLeftEvent extends UserEvent {

    public UserLeftEvent(UserName userName) {
        super(userName);
    }

}
