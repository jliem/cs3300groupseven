package colab.server;

import java.io.Serializable;
import java.rmi.RemoteException;

import colab.channel.Channel;


public class ChannelManager implements ChannelManagerInterface, Serializable {

	public static final long serialVersionUID = 1L;
	
	/** {@inheritDoc} */
	public Channel getChannel(String channelName) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
