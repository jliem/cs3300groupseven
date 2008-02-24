package colab.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelName;
import colab.common.channel.ChatChannelData;
import colab.common.channel.ChatDataCollection;
import colab.common.identity.LockIdentifier;

public class ServerChatChannel extends ServerChannel {

    /** Serialization version number. */
    private static final long serialVersionUID = 1L;

    protected ChatDataCollection messages;

    public ServerChatChannel(ChannelName name) throws RemoteException {
        super(name);
        // TODO Auto-generated constructor stub
    }

    public void add(ChannelData data) {
        // TODO Auto-generated method stub

    }

    public void addAll(List<ChannelData> data) {
        // TODO Auto-generated method stub

    }

    public int getDataCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    public List<ChannelData> getFirst(int count) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<ChannelData> getFrom(int startIndex, int count) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<ChannelData> getLast(int count) {
        return messages.getLast(count);
    }

    public void getLock(LockIdentifier lockId) {
        // TODO Auto-generated method stub
    }

    public List<ChannelData> getRange(int startIndex, int endIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<ChannelData> getTo(int endIndex, int count) {
        // TODO Auto-generated method stub
        return null;
    }

    public void giveLock(LockIdentifier lockId) {
        // TODO Auto-generated method stub

    }

    public void lock(LockIdentifier lockId, String username) {
        // TODO Auto-generated method stub

    }

    public void unlock(LockIdentifier lockId, String username) {
        // TODO Auto-generated method stub
    }

}
