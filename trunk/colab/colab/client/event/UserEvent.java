package colab.client.event;

import colab.common.naming.UserName;

/**
 * An event for handling a change in the user's status (joined or left).
 * @author JL
 *
 */
public abstract class UserEvent {

    private final UserName userName;

    public UserEvent(final UserName userName) {
        this.userName = userName;
    }

    public UserName getUserName() {
        return this.userName;
    }
}
