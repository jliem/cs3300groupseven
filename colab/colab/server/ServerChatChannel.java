package colab.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelName;
import colab.common.channel.ChatChannelData;
import colab.common.identity.LockIdentifier;

public class ServerChatChannel extends ServerChannel {

    protected TreeSet<ChatChannelData> messages;

    /** Serialization version number. */
    private static final long serialVersionUID = 1L;

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

        Iterator<ChatChannelData> it = messages.descendingIterator();
        List<ChannelData> result = new ArrayList<ChannelData>(count);
        while (it.hasNext()) {
            result.add(0, it.next());
        }
        return result;

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
