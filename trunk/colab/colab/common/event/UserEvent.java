package colab.common.event;

import colab.common.naming.UserName;

/**
 * An event for handling a change in the user's status (joined or left).
 */
public abstract class UserEvent {

    /** The name of the user. */
    private final UserName userName;

    /**
     * Constructs a new UserEvent.
     *
     * @param userName the name of the user
     */
    public UserEvent(final UserName userName) {
        this.userName = userName;
    }

    /**
     * @return the name of the user
     */
    public final UserName getUserName() {
        return this.userName;
    }

}
