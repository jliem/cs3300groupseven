package colab.client.event;

public interface UserListener {

    void userJoined(UserJoinedEvent ue);

    void userLeft(UserLeftEvent ue);
}
