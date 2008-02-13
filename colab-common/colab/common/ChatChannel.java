package colab.common;

import java.util.Stack;

public class ChatChannel extends Channel {

	protected Stack<ChatChannelData> channelData;
	
	public ChatChannel() {
		channelData = new Stack<ChatChannelData>();
	}
	
	@Override
	public void add(ChannelData data) {

		ChatChannelData chatData = (ChatChannelData)data;
		
		channelData.push(chatData);
	}

	@Override
	public boolean remove(ChannelData data) {
		return channelData.remove(data);
	}


}
