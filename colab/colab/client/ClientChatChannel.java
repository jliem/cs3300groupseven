package colab.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.ChannelType;
import colab.common.channel.ChatChannelData;
import colab.common.channel.ChatDataCollection;
import colab.common.naming.ChannelName;

public class ClientChatChannel extends ClientChannel {

    private ArrayList <ActionListener> listeners;
    private int newMessages = 0;

    protected ChatDataCollection messages;

    public ClientChatChannel(final ChannelName name) throws RemoteException {
        super(name);
        messages = new ChatDataCollection();
        listeners = new ArrayList <ActionListener> ();
    }

    public void add(final ChannelData data) throws RemoteException {
        ChatChannelData chatData = (ChatChannelData) data;
        messages.add(chatData);
        newMessages++;
        fireActionPerformed(new ActionEvent(
                this, ActionEvent.ACTION_FIRST, "Message Added"));
    }

    protected void fireActionPerformed(ActionEvent e) {
        for(ActionListener l : listeners) {
            l.actionPerformed(e);
        }
    }

    public void addActionListener(ActionListener l) {
        listeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        listeners.remove(l);
    }

    public ChannelDescriptor getChannelDescriptor() {
        return new ChannelDescriptor(this.getId(), ChannelType.CHAT);
    }

    public List<String> getNewMessages() {
        System.err.println("Getting new messages");
        ArrayList <String> list = new ArrayList <String>();

        List <ChatChannelData> channelDataList = messages.getLast(newMessages);

        for(ChatChannelData d : channelDataList){
            list.add(d.getText());
        }

        newMessages = 0;
        return list;
    }
}
