package colab.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.ChannelType;
import colab.common.channel.ChatChannelData;
import colab.common.channel.ChatDataSet;
import colab.common.naming.ChannelName;

public class ClientChatChannel extends ClientChannel {

	private ArrayList<ActionListener> listeners;

	private int newMessages = 0;

	protected ChatDataSet messages;

	public ClientChatChannel(final ChannelName name) throws RemoteException {
		super(name);
		messages = new ChatDataSet();
		listeners = new ArrayList<ActionListener>();
	}

	public void add(final ChannelData data) throws RemoteException {
		ChatChannelData chatData = (ChatChannelData) data;
		messages.add(chatData);
		newMessages++;
		fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_FIRST,
				"Message Added"));
	}

	protected void fireActionPerformed(final ActionEvent e) {
		for (ActionListener l : listeners) {
			l.actionPerformed(e);
		}
	}

	public void addActionListener(final ActionListener l) {
		listeners.add(l);
	}

	public void removeActionListener(final ActionListener l) {
		listeners.remove(l);
	}

	public ChannelDescriptor getChannelDescriptor() {
		return new ChannelDescriptor(this.getId(), ChannelType.CHAT);
	}

	public List<ChatChannelData> getLocalMessages() {
		return messages.getLast(-1);
	}

	public int getLocalNumMessages() {
		return messages.size();
	}

	public List<ChatChannelData> getNewMessages() {
		List <ChatChannelData> list = messages.getLast(newMessages);
		newMessages = 0;
		return list;
	}
}
