package colab.common.channel.remote;

import java.rmi.Remote;
import java.util.List;

import colab.common.channel.ChannelData;
import colab.common.identity.LockIdentifier;

/**
 * A remote object passed to the server, so that
 * the server can send messages to the client.
 */
public interface ChannelInterface extends Remote {

    int getDataCount();

    List<ChannelData> getLast(int count);

    List<ChannelData> getFirst(int count);

    List<ChannelData> getFrom(int startIndex, int count);

    List<ChannelData> getTo(int endIndex, int count);

    List<ChannelData> getRange(int startIndex, int endIndex);

    void add(ChannelData data);

    void addAll(List<ChannelData> data);

    boolean getLock(LockIdentifier lockId);

    boolean releaseLock(LockIdentifier lockId);

}
