package colab.common.event;

import colab.common.naming.UserName;

/**
 * Indicates that a user has left.
 */
public class UserLeftEvent extends UserEvent {

    /**
     * Constructs a new UserLeftEvent.
     *
     * @param userName the name of the user
     */
    public UserLeftEvent(final UserName userName) {
        super(userName);
    }

}
