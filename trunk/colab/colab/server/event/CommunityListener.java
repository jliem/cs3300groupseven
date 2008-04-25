package colab.server.event;

/**
 * A listener which can be notifier of Community events.
 */
public interface CommunityListener {

    /**
     * Called when a community event is fired.
     *
     * @param event the event
     */
    void handleEvent(CommunityEvent event);

}
