package colab.common.event.user;

import colab.common.naming.UserName;

/**
 * Indicates that a user has joined.
 */
public class UserJoinedEvent extends UserEvent {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new UserJoinedEvent.
     *
     * @param userName the name of the user
     */
    public UserJoinedEvent(final UserName userName) {
        super(userName);
    }

}
