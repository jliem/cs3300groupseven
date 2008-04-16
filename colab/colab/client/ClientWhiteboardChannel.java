package colab.client;

import java.rmi.RemoteException;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.ChannelDescriptor;
import colab.common.naming.ChannelName;

public class ClientWhiteboardChannel extends ClientChannel {

    public static final long serialVersionUID = 1;
    
    public ClientWhiteboardChannel(final ChannelName name) throws RemoteException {
        super(name);
        
    }
    
    @Override
    public ChannelDataSet getChannelData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ChannelDescriptor getChannelDescriptor() {
        // TODO Auto-generated method stub
        return null;
    }

    public void add(ChannelData data) throws RemoteException {
        // TODO Auto-generated method stub

    }

}
