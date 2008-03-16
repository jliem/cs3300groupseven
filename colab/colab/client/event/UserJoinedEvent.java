package colab.client.event;

import colab.common.naming.UserName;

/**
 * Indicates that a user has joined.
 */
public class UserJoinedEvent extends UserEvent {

    /**
     * Constructs a new UserJoinedEvent.
     *
     * @param userName the name of the user
     */
    public UserJoinedEvent(final UserName userName) {
        super(userName);
    }

}
