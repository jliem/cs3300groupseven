package colab.common;

import java.util.Date;

public class ChatChannelData extends ChannelData {

	private String text;
	
	public ChatChannelData(String text, User creator) {
		this.text = text;
		this.creator = creator;
		
		
		// TODO: Set timestamp
	}

	
	
}
