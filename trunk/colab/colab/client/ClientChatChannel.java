package colab.client;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Stack;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelName;
import colab.common.channel.ChatChannelData;
import colab.common.identity.LockIdentifier;

public class ClientChatChannel extends ClientChannel {

    protected Stack<ChatChannelData> channelData;

    public ClientChatChannel(final ChannelName name) throws RemoteException {
        super(name);
        channelData = new Stack<ChatChannelData>();
    }

    @Override
    public void add(final ChannelData data) {

        ChatChannelData chatData = (ChatChannelData)data;

        channelData.push(chatData);
    }

    @Override
    public boolean remove(final ChannelData data) {
        return channelData.remove(data);
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
        // TODO Auto-generated method stub
        return null;
    }

    public boolean getLock(LockIdentifier lockId) {
        // TODO Auto-generated method stub
        return false;
    }

    public List<ChannelData> getRange(int startIndex, int endIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<ChannelData> getTo(int endIndex, int count) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean releaseLock(LockIdentifier lockId) {
        // TODO Auto-generated method stub
        return false;
    }

}
