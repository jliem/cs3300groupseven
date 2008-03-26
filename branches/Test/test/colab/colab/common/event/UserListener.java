package colab.common.event;

/**
 * A UserListener is notified when a user joins or leaves
 * the object on which it is listening.
 */
public interface UserListener {

    /**
     * @param joinedEvent the join event
     */
    void handleUserEvent(UserJoinedEvent joinedEvent);

    /**
     * @param leftEvent the leave event
     */
    void handleUserEvent(UserLeftEvent leftEvent);

}
