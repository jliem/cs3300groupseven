package colab.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import colab.channel.Channel;


public class ChannelManager extends UnicastRemoteObject
        implements ChannelManagerInterface {

    public static final long serialVersionUID = 1L;
    
    public ChannelManager() throws RemoteException {
        
    }
    
    /** {@inheritDoc} */
    public Channel getChannel(String channelName) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

}
