package colab.client.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import colab.common.channel.ChannelDescriptor;

public interface ColabClientInterface extends Remote {

    public void channelAdded(ChannelDescriptor channelDescriptor)
            throws RemoteException;

}
