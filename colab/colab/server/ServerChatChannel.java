package colab.server;

import java.rmi.RemoteException;

import colab.common.channel.ChannelName;

/**
 * @author jl
 *
 */
public class ServerChatChannel extends ServerChannel {

	private static final long serialVersionUID = 1L;

	public ServerChatChannel(ChannelName name) throws RemoteException {
		super(name);
		// TODO Auto-generated constructor stub
	}
}
