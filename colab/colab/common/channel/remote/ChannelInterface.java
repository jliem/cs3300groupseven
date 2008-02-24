package colab.common.channel.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import colab.common.channel.ChannelData;
import colab.common.identity.LockIdentifier;

/**
 * A remote object passed to the server, so that
 * the server can send messages to the client.
 */
public interface ChannelInterface extends Remote {

    int getDataCount() throws RemoteException;

    List<ChannelData> getLast(int count)
        throws RemoteException;

    List<ChannelData> getFirst(int count)
        throws RemoteException;

    List<ChannelData> getFrom(int startIndex, int count)
        throws RemoteException;

    List<ChannelData> getTo(int endIndex, int count)
        throws RemoteException;

    List<ChannelData> getRange(int startIndex, int endIndex)
        throws RemoteException;

    void add(ChannelData data) throws RemoteException;

    void addAll(List<ChannelData> data)
        throws RemoteException;

    void getLock(LockIdentifier lockId)
        throws RemoteException;

    void giveLock(LockIdentifier lockId)
        throws RemoteException;

    void lock(LockIdentifier lockId, String username)
        throws RemoteException;

    void unlock(LockIdentifier lockId, String username)
        throws RemoteException;
}
