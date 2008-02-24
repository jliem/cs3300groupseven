package colab.client;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelName;
import colab.common.channel.ChatChannelData;
import colab.common.identity.LockIdentifier;

public class ClientChatChannel extends ClientChannel {

    protected TreeSet<ChatChannelData> messages;

    public ClientChatChannel(final ChannelName name) throws RemoteException {

        super(name);

        messages = new TreeSet<ChatChannelData>(
            new Comparator<ChatChannelData>() {
                public int compare(final ChatChannelData arg0,
                        final ChatChannelData arg1) {
                    return arg0.getTimestamp().compareTo(arg1.getTimestamp());
                }
            }
        );

    }

    public void add(final ChannelData data) throws RemoteException {
        ChatChannelData chatData = (ChatChannelData) data;
        messages.add(chatData);
    }

    public void addAll(List<ChannelData> data) throws RemoteException {
        for (ChannelData d : data) {
            add(d);
        }
    }

    public int getDataCount() throws RemoteException {
        return messages.size();
    }

    public List<ChannelData> getFirst(int count) throws RemoteException {
        throw new UnsupportedOperationException();
    }

    public List<ChannelData> getFrom(int startIndex, int count)
            throws RemoteException {
        throw new UnsupportedOperationException();
    }

    public List<ChannelData> getLast(int count) throws RemoteException {
        throw new UnsupportedOperationException();
    }

    public List<ChannelData> getRange(int startIndex, int endIndex)
            throws RemoteException {
        throw new UnsupportedOperationException();
    }

    public List<ChannelData> getTo(int endIndex, int count)
            throws RemoteException {
        throw new UnsupportedOperationException();
    }

    public void getLock(LockIdentifier lockId)
            throws RemoteException {
        throw new UnsupportedOperationException();
    }

    public void giveLock(LockIdentifier lockId)
            throws RemoteException {
        throw new UnsupportedOperationException();
    }

    public void lock(LockIdentifier lockId, String username)
            throws RemoteException {
        throw new UnsupportedOperationException();
    }

    public void unlock(LockIdentifier lockId, String username)
            throws RemoteException {
        throw new UnsupportedOperationException();
    }

}
