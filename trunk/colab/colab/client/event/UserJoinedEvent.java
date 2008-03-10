package colab.client.event;

import colab.common.naming.UserName;

public class UserJoinedEvent extends UserEvent {

    public UserJoinedEvent(UserName userName) {
        super(userName);
    }

}
