package colab.client;

import java.rmi.RemoteException;

import colab.common.channel.ChannelData;
import colab.common.channel.ChannelDataSet;
import colab.common.channel.ChannelDescriptor;
import colab.common.channel.type.WhiteboardChannelType;
import colab.common.channel.whiteboard.Whiteboard;
import colab.common.naming.ChannelName;

public class ClientWhiteboardChannel extends ClientChannel {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private Whiteboard whiteboard;

    public ClientWhiteboardChannel(final ChannelName name)
            throws RemoteException {

        super(name);

        whiteboard = new Whiteboard();

    }

    public Whiteboard getWhiteboard() {
        return whiteboard;
    }

    @Override
    public ChannelDataSet getChannelData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ChannelDescriptor getChannelDescriptor() {
        return new ChannelDescriptor(this.getId(), new WhiteboardChannelType());
    }

    public void add(final ChannelData data) throws RemoteException {
        // TODO Auto-generated method stub

    }

}
